package tangx.jsite.site.core.persistence.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import tangx.jsite.site.core.persistence.dao.BaseDao;
import tangx.jsite.site.core.persistence.entity.BaseEntity;

import com.google.common.collect.Maps;

public class PersistenceUtil {
	
	// 用于存放各实体的属性名-列名关系
	private static final Map<Class<?>, Map<String, String>> columnMap = Maps.newConcurrentMap();

	/**
	 * 获取表名。实体需定义@Table(name)，如果没有定义则取类名为表名
	 * @param clazz 实体class
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (table != null) {
			if (StringUtils.isNotBlank(table.name())) {
				return table.name();
			} 
		}
		return clazz.getSimpleName();
	}
	
	/**
	 * 获取@Id的属性名
	 * 
	 * @return
	 */
	public static String getIdFieldName(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class))
				return field.getName();
		}
		// 没有找到id，向上找父类的id
		return getParentIdFieldName(clazz.getSuperclass());
	}
	
	/**
	 * 查询父类@Id的属性名
	 * 
	 * @return
	 */
	private static String getParentIdFieldName(Class<?> clazz) {
		if (BaseEntity.class.equals(clazz)) {
			// 如果向上找到MybatisEntity基类则返回默认的'id'
			return BaseEntity.ID_FIELD_NAME;
		}
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class))
				return field.getName();
		}
		// 继续向上找父类id
		return getParentIdFieldName(clazz.getSuperclass());
	}
	
	/**
	 * 获取主键对应的数据库字段名称
	 * 
	 * @return
	 */
	public static String getIdColumnName(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class)) {
				if (field.isAnnotationPresent(Column.class)) {
					Column c = field.getAnnotation(Column.class);
					if (StringUtils.isNotBlank(c.name())) {
						return c.name();
					}
				}
				return field.getName();
			}
		}
		return getParentIdColumnName(clazz.getSuperclass());
	}

	/**
	 * 查询父类的主键数据库字段名称
	 * 
	 * @return
	 */
	private static String getParentIdColumnName(Class<?> clazz) {
		if (BaseEntity.class.equals(clazz)) {
			// 如果向上找到MybatisEntity基类则返回默认的'id'
			return BaseEntity.ID_FIELD_NAME;
		}
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class)) {
				if (field.isAnnotationPresent(Column.class)) {
					Column c = field.getAnnotation(Column.class);
					if (StringUtils.isNotBlank(c.name())) {
						return c.name();
					}
				}
				return field.getName();
			}
		}
		return getParentIdColumnName(clazz.getSuperclass());
	}
	
	/**
	 * 根据方法名判断是否基类dao接口的select方法
	 * @param methodName
	 * @return
	 */
	public static boolean isBaseDaoSelectMethod(String methodName){
		Method[] methods = BaseDao.class.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("select") && methodName.equals(method.getName())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 计算实体中标记为@Column的属性，以属性名为key，数据库字段名为value，放到Map中(这里排除@Id字段，即使该字段也有@Column)
	 */
	public static void caculationColumnList(Class<?> clazz) {
		if (columnMap.containsKey(clazz)) {
			return;
		}
		Map<String, String> columnDefs = Maps.newHashMap();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				if (field.isAnnotationPresent(Id.class)) {
					continue;
				}
				columnDefs.put(field.getName(), getColumnName(field));
			}
		}
		// 获取父类属性
		fields = clazz.getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				if (field.isAnnotationPresent(Id.class)) {
					continue;
				}
				columnDefs.put(field.getName(), getColumnName(field));
			}
		}
		columnMap.put(clazz, columnDefs);
	}
	
	/**
	 * 获取需要insert的数据库字段列表 (忽略空值字段)
	 * 
	 * @return column1,column2,...
	 */
	public static String insertColumnNameList(Object obj) {
		caculationColumnList(obj.getClass());
		StringBuilder sb = new StringBuilder();
		Map<String, String> columnDefs = columnMap.get(obj.getClass());
		int i = 0;
		for (String fieldName : columnDefs.keySet()) {
			if (isNull(obj, fieldName)) {
				continue;
			}
			if (i++ != 0) {
				sb.append(',');
			}
			sb.append(columnDefs.get(fieldName));
		}
		return sb.toString();
	}

	/**
	 * 获取需要insert的属性列表(忽略空值字段)
	 * 
	 * @return #{field1},#{field2},...
	 */
	public static String insertFieldNameList(Object obj) {
		caculationColumnList(obj.getClass());
		StringBuilder sb = new StringBuilder();
		Map<String, String> columnDefs = columnMap.get(obj.getClass());
		int i = 0;
		for (String fieldName : columnDefs.keySet()) {
			if (isNull(obj, fieldName)) {
				continue;
			}
			if (i++ != 0) {
				sb.append(',');
			}
			sb.append("#{").append(fieldName).append('}');
		}
		return sb.toString();
	}

	/**
	 * 获取update的SQL(忽略空值字段)
	 * 
	 * @return column1=#{field1},column2=#{field2},...
	 */
	public static String updateSql(Object obj) {
		caculationColumnList(obj.getClass());
		StringBuilder sb = new StringBuilder();
		Map<String, String> columnDefs = columnMap.get(obj.getClass());
		int i = 0;
		for (String fieldName : columnDefs.keySet()) {
			if (isNull(obj, fieldName)) {
				continue;
			}
			if (i++ != 0) {
				sb.append(',');
			}
			sb.append(columnDefs.get(fieldName)).append("=#{")
					.append(fieldName).append('}');
		}
		return sb.toString();
	}
	
	/**
	 * 根据字段名获取对应列名
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static String getFieldNameByColumnName(Class<?> clazz, String fieldName) {
		caculationColumnList(clazz);
		Map<String, String> columnDefs = columnMap.get(clazz);
		return columnDefs.get(fieldName);
	}
	
	/**
	 * 根据类获取对应的属性名-列名关系
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Map<String, String> getFieldColumnMapping(Class<?> clazz) {
		caculationColumnList(clazz);
		return columnMap.get(clazz);
	}
	
	/**
	 * 获取@Column对应的数据库列名
	 * 
	 * @return
	 */
	private static String getColumnName(Field field) {
		Column c = field.getAnnotation(Column.class);
		if (StringUtils.isEmpty(c.name())) {
			return field.getName();
		} else {
			return c.name();
		}
	}

	/**
	 * 判断字段值是否为空
	 * 
	 * @return
	 */
	private static boolean isNull(Object obj, String fieldName) {
		try {
			return FieldUtils.readField(obj, fieldName, true) == null ? true
					: false;
		} catch (Exception e) {
			throw new RuntimeException("isNull error");
		}
	}
}
