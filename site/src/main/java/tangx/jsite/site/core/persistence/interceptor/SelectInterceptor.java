package tangx.jsite.site.core.persistence.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import tangx.jsite.site.core.persistence.dao.BaseDao;
import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.core.persistence.util.PersistenceUtil;

/**
 * Mybatis拦截器，作用如下：<br>
 * 1、基类dao接口的查询方法必须指定对应的resultMap<br>
 * 2、如果参数中有 {@link Page} 对象，那么执行分页查询。注意如果接口有多个参数则page参数必须@Param(PAGE_KEY)
 * 
 * @author tangx
 * 
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
@Component
public class SelectInterceptor implements Interceptor {

	/** hsqldb,mysql,oracle... */
	private String dialect = "mysql";
	
	// 动态生成的resultMap名，注意在xml中不能重名
	private static final String RESULTMAP_NAME = "GenerateResultMap";

	@SuppressWarnings("rawtypes")
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation
					.getTarget();
			StatementHandler handler = (StatementHandler) FieldUtils.readField(
					statementHandler, "delegate", true);
			MappedStatement ms = (MappedStatement) FieldUtils.readField(
					handler, "mappedStatement", true);

			BoundSql boundSql = handler.getBoundSql();
			Object paramObj = boundSql.getParameterObject();
			String originalSql = boundSql.getSql().toLowerCase();

			// 如果是基类dao接口的查询方法必须指定对应的resultMap
			if (PersistenceUtil.isBaseDaoSelectMethod(getDaoMethodName(ms))) {
				String daoClassName = getDaoClassName(ms);
				Class<?> entityClazz = getEntityClass(daoClassName);
				List<ResultMap> resultMaps = new ArrayList<ResultMap>();
				resultMaps.add(generateResultMap(daoClassName + "." + RESULTMAP_NAME, entityClazz, ms.getConfiguration()));
				FieldUtils.writeField(ms, "resultMaps", resultMaps, true);
			}

			// 如果有page参数且sql语句中有select，则表明此sql语句需要做分页处理
			Page<?> page = null;
			if (paramObj instanceof Page && paramObj != null) {
				page = (Page<?>) paramObj;
			} else if (paramObj instanceof Map) {
				Map map = (HashMap) paramObj;
				if (map.containsKey(BaseDao.PAGE_KEY) && map.get(BaseDao.PAGE_KEY) != null) {
					page = (Page<?>) map.get(BaseDao.PAGE_KEY);
				}
			}
			if (originalSql.indexOf("select") != -1 && page != null) {
				int total = queryTotal(invocation, ms, boundSql, paramObj, originalSql);
				page.setRecordsTotal(total);
				page.setRecordsFiltered(total);
				FieldUtils.writeField(boundSql, "sql",
						pageSql(originalSql, page), true);
			}

		}
		return invocation.proceed();
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		// dialect = p.getProperty("dialect");
	}

	/**
	 * 获取MappedStatement对应的Dao类名
	 */
	private String getDaoClassName(MappedStatement ms) {
		String id = ms.getId();
		String daoFullName = "";
		if (StringUtils.isNotBlank(id) && (id.indexOf(".") != -1)) {
			daoFullName = id.substring(0, StringUtils.lastIndexOf(id, "."));
		}
		return daoFullName;
	}

	/**
	 * 获取MappedStatement对应的方法名
	 */
	private String getDaoMethodName(MappedStatement ms) {
		String id = ms.getId();
		String methodName = "";
		if (StringUtils.isNotBlank(id) && (id.indexOf(".") != -1)) {
			methodName = id.substring(StringUtils.lastIndexOf(id, ".") + 1);
		}
		return methodName;
	}

	/**
	 * 生成特定数据库的分页语句
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	private String pageSql(String sql, Page<?> page) {
		if (page == null || dialect == null || dialect.equals("")) {
			return sql;
		}
		StringBuilder sb = new StringBuilder();
		// 计算起始序号，普通分页用currentResult，dataTable分页用start
		int start = page.getStart()!=0?page.getStart():page.getCurrentResult();
		if ("hsqldb".equals(dialect)) {
			String s = sql;
			sb.append("select limit ");
			sb.append(start);
			sb.append(" ");
			sb.append(page.getSize());
			sb.append(" ");
			sb.append(s.substring(6));
		} else if ("mysql".equals(dialect)) {
			sb.append(sql);
			sb.append(" limit " + start + ","
					+ page.getSize());
		} else if ("oracle".equals(dialect)) {
			sb.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
			sb.append(sql);
			sb.append(")  tmp_tb where ROWNUM<=");
			sb.append(start + page.getSize());
			sb.append(") where row_id>");
			sb.append(start);
		} else {
			throw new IllegalArgumentException(
					"SelectInterceptor error:does not support " + dialect);
		}
		return sb.toString();
	}

	/**
	 * 查询记录集总数
	 * 
	 * @param invocation
	 * @param ms
	 * @param boundSql
	 * @param param
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private int queryTotal(Invocation invocation, MappedStatement ms,
			BoundSql boundSql, Object param, String sql) throws SQLException {
		Connection conn = (Connection) invocation.getArgs()[0];
		int index = sql.indexOf("from");
		if (index == -1) {
			throw new RuntimeException(
					"SelectInterceptor error:statement has no 'from' key word");
		}
		String countSql = "select count(*) " + sql.substring(index);
		BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), countSql,
				boundSql.getParameterMappings(), param);
		ParameterHandler parameterHandler = new DefaultParameterHandler(ms,
				param, countBoundSql);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(countSql);
			// 通过parameterHandler给PreparedStatement对象设置参数
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			rs.close();
			pstmt.close();
		}
		return count;
	}

	/**
	 * 根据Dao类名获取泛型实体的具体类
	 * 
	 * @return
	 */
	private Class<?> getEntityClass(String daoClassName) {
		try {
			Class<?>[] clazzs = GenericTypeResolver.resolveTypeArguments(
					Class.forName(daoClassName), BaseDao.class);
			return (Class<?>) clazzs[0];
		} catch (Exception e) {
			throw new RuntimeException(
					"SelectInterceptor getEntityClass error:" + e.getMessage());
		}

	}

	/**
	 * 根据实体的属性名-列名映射关系生成ResultMap
	 * 
	 * @param id ResultMap标识
	 * @param clazz 实体类
	 * @param configuration 配置对象
	 */
	private ResultMap generateResultMap(String id, Class<?> clazz,
			Configuration configuration) {
		// 第一次生成时会加入到配置缓存中，这里判断配置里已经有了则直接返回
		if(configuration.hasResultMap(id)){
			return configuration.getResultMap(id);
		}
		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		// 处理id列
		Class<?> idType = resolveResultJavaType(clazz,
				PersistenceUtil.getIdFieldName(clazz), null);
		List<ResultFlag> flags = new ArrayList<ResultFlag>();
		flags.add(ResultFlag.ID);
		resultMappings.add(generateResultMapping(configuration,
				PersistenceUtil.getIdFieldName(clazz),
				PersistenceUtil.getIdColumnName(clazz), idType, flags));
		// 处理普通列
		Map<String, String> map = PersistenceUtil.getFieldColumnMapping(clazz);
		for (String key : map.keySet()) {
			Class<?> columnTypeClass = resolveResultJavaType(clazz, key, null);
			resultMappings.add(generateResultMapping(configuration, key,
					map.get(key), columnTypeClass, null));
		}
		// 构建ResultMap
		ResultMap.Builder resultMapBuilder = new ResultMap.Builder(
				configuration, id, clazz, resultMappings, null);
		ResultMap rm = resultMapBuilder.build();
		// 放到配置中
		configuration.addResultMap(rm);
		return rm;
	}

	/**
	 * 构建ResultMapping
	 * 
	 * @param configuration
	 * @param property
	 * @param column
	 * @param javaType
	 * @param flags
	 * @return
	 */
	private ResultMapping generateResultMapping(Configuration configuration,
			String property, String column, Class<?> javaType,
			List<ResultFlag> flags) {
		ResultMapping.Builder builder = new ResultMapping.Builder(
				configuration, property, column, javaType);
		builder.flags(flags == null ? new ArrayList<ResultFlag>() : flags);
		builder.composites(new ArrayList<ResultMapping>());
		builder.notNullColumns(new HashSet<String>());
		return builder.build();
	}

	private Class<?> resolveResultJavaType(Class<?> resultType,
			String property, Class<?> javaType) {
		if (javaType == null && property != null) {
			try {
				MetaClass metaResultType = MetaClass.forClass(resultType);
				javaType = metaResultType.getSetterType(property);
			} catch (Exception e) {
				// ignore, following null check statement will deal with the
				// situation
			}
		}
		if (javaType == null) {
			javaType = Object.class;
		}
		return javaType;
	}

}