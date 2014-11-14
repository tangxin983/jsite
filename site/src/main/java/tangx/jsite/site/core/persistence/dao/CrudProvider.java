package tangx.jsite.site.core.persistence.dao;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import tangx.jsite.site.core.persistence.util.PersistenceUtil;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

/**
 * 增删改查模板
 * 
 * @author tangx
 * 
 */
public class CrudProvider<T> {

	public String select(final Class<T> clazz) {
		return new SQL() {
			{
				SELECT("*");
				FROM(PersistenceUtil.getTableName(clazz));
			}
		}.toString();
	}

	@SuppressWarnings("unchecked")
	public String selectById(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<T> clazz = (Class<T>) parameter.get(BaseDao.CLASS_KEY);
				SELECT("*");
				FROM(PersistenceUtil.getTableName(clazz));
				WHERE(PersistenceUtil.getIdColumnName(clazz) + " = '" + parameter.get(BaseDao.ID_KEY) + "'");
			}
		}.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String complicatedSelect(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<T> clazz = (Class<T>) parameter.get(BaseDao.CLASS_KEY);
				// 相等查询条件（key=value）
				Map<String, Object> paramMap = Maps.newHashMap();
				if(parameter.containsKey(BaseDao.PARA_KEY)){
					paramMap = (Map<String, Object>) parameter.get(BaseDao.PARA_KEY);
				}
				// 复杂查询条件（<列名,运算符,列值>）
				Table<String, String, Object> comMap = HashBasedTable.create();
				if(parameter.containsKey(BaseDao.COM_KEY)){
					comMap = (Table<String, String, Object>) parameter.get(BaseDao.COM_KEY);
				}
				// 排序条件
				Map<String, String> orderMap = Maps.newHashMap();
				if(parameter.containsKey(BaseDao.ORDER_KEY)){
					orderMap = (Map<String, String>) parameter.get(BaseDao.ORDER_KEY);
				}
				SELECT("*");
				FROM(PersistenceUtil.getTableName(clazz));
				if(paramMap != null) {
					for(String key : paramMap.keySet())  {
						// 用map key查找数据库列名 如果找不到则以key作为列名
						String columnName = PersistenceUtil.getFieldNameByColumnName(clazz, key);
						if(StringUtils.isBlank(columnName)){
							columnName = key;
						}
						if(paramMap.get(key) != null && (StringUtils.isNotBlank(paramMap.get(key).toString()))){
							WHERE(columnName + "= #{ " + BaseDao.PARA_KEY + "." + key + "}");
						}
					}
				}
				if(comMap != null) {
					for (Cell<String, String, Object> cell : comMap.cellSet()) {
						// 用rowKey查找数据库列名 如果找不到则以rowKey作为列名
						String columnName = PersistenceUtil.getFieldNameByColumnName(clazz, cell.getRowKey());
						if(StringUtils.isBlank(columnName)){
							columnName = cell.getRowKey();
						}
						if(StringUtils.isNotBlank(cell.getValue().toString())){
							if(cell.getColumnKey().equalsIgnoreCase("like")){
								WHERE(columnName + " like '%" + cell.getValue() + "%'");
							}else{
								WHERE(columnName + cell.getColumnKey() + "'" + cell.getValue() + "'");
							}
						}
					}
				}
				if(orderMap != null) {
					for(String key : orderMap.keySet())  {
						// 用map key查找数据库列名 如果找不到则以key作为列名
						String columnName = PersistenceUtil.getFieldNameByColumnName(clazz, key);
						if(StringUtils.isBlank(columnName)){
							columnName = key;
						}
						ORDER_BY(columnName + " " + StringUtils.defaultString(orderMap.get(key)));
					}
				}
			}
		}.toString();
	}
	
	public String count(final Class<T> clazz) {
		return new SQL() {
			{
				SELECT("count(*)");
				FROM(PersistenceUtil.getTableName(clazz));
			}
		}.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String countByCondition(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<T> clazz = (Class<T>) parameter.get(BaseDao.CLASS_KEY);
				Map<String, Object> paramMap = Maps.newHashMap();
				if(parameter.containsKey(BaseDao.PARA_KEY)){
					paramMap = (Map<String, Object>) parameter.get(BaseDao.PARA_KEY);
				}
				SELECT("count(*)");
				FROM(PersistenceUtil.getTableName(clazz));
				if(paramMap != null){
					for(String key : paramMap.keySet())  {
						// 用map key查找数据库列名 如果找不到则以key作为列名
						String columnName = PersistenceUtil.getFieldNameByColumnName(clazz, key);
						if(StringUtils.isBlank(columnName)){
							columnName = key;
						}
						if(paramMap.get(key) != null && (StringUtils.isNotBlank(paramMap.get(key).toString()))){
							WHERE(columnName + "= #{ " + BaseDao.PARA_KEY + "." + key + "}");
						}
					}
				}
			}
		}.toString();
	}

	public String insert(final T t) {
		return new SQL() {
			{
				INSERT_INTO(PersistenceUtil.getTableName(t.getClass()));
				VALUES(PersistenceUtil.getIdColumnName(t.getClass()) + "," + PersistenceUtil.insertColumnNameList(t),
						"#{" + PersistenceUtil.getIdFieldName(t.getClass()) + "}," + PersistenceUtil.insertFieldNameList(t));

			}
		}.toString();
	}

	public String insertWithoutId(final T t) {
		return new SQL() {
			{
				INSERT_INTO(PersistenceUtil.getTableName(t.getClass()));
				VALUES(PersistenceUtil.insertColumnNameList(t), PersistenceUtil.insertFieldNameList(t));
			}
		}.toString();
	}

	public String update(final T t) {
		return new SQL() {
			{
				UPDATE(PersistenceUtil.getTableName(t.getClass()));
				SET(PersistenceUtil.updateSql(t));
				WHERE(PersistenceUtil.getIdColumnName(t.getClass()) + " = #{" + PersistenceUtil.getIdFieldName(t.getClass()) + "}");
			}
		}.toString();
	}

	@SuppressWarnings("unchecked")
	public String deleteById(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<T> clazz = (Class<T>) parameter.get(BaseDao.CLASS_KEY);
				DELETE_FROM(PersistenceUtil.getTableName(clazz));
				WHERE(PersistenceUtil.getIdColumnName(clazz) + " = '" + parameter.get(BaseDao.ID_KEY) + "'");
			}
		}.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String deleteByCondition(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<T> clazz = (Class<T>) parameter.get(BaseDao.CLASS_KEY);
				Map<String, Object> paramMap = Maps.newHashMap();
				if(parameter.containsKey(BaseDao.PARA_KEY)){
					paramMap = (Map<String, Object>) parameter.get(BaseDao.PARA_KEY);
				}
				DELETE_FROM(PersistenceUtil.getTableName(clazz));
				if(paramMap != null) {
					for(String key : paramMap.keySet())  {
						// 用map key查找数据库列名 如果找不到则以key作为列名
						String columnName = PersistenceUtil.getFieldNameByColumnName(clazz, key);
						if(StringUtils.isBlank(columnName)){
							columnName = key;
						}
						if(paramMap.get(key) != null && (StringUtils.isNotBlank(paramMap.get(key).toString()))){
							WHERE(columnName + "= #{ " + BaseDao.PARA_KEY + "." + key + "}");
						}
					}
				}
			}
		}.toString();
	}
}
