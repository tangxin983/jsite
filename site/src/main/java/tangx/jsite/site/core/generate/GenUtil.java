package tangx.jsite.site.core.generate;

import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 代码生成辅助类
 * 
 * @author tangx
 *
 */
public class GenUtil {
	
	private static Logger logger = LoggerFactory.getLogger(GenUtil.class);

	private GenUtil() {

	}

	private static Properties prop = new Properties();

	static {
		InputStream in = GenUtil.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			prop.load(in);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 从数据库中读出表的字段名、字段类型、是否允许为空等信息
	 * 
	 * @param tableName
	 * @return
	 */
	public static List<Map<String, String>> getRsmd(String tableName) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(prop.getProperty("jdbc.driver"));
		dataSource.setUrl(prop.getProperty("jdbc.url"));
		dataSource.setUsername(prop.getProperty("jdbc.username"));
		dataSource.setPassword(prop.getProperty("jdbc.password"));
		final List<Map<String, String>> columns = Lists.newArrayList();
		try {
			DatabaseMetaData dbmd = dataSource.getConnection().getMetaData();
			ResultSet rs = dbmd.getColumns(null, "%", tableName, "%");
			while (rs.next()) {
				// 由于实体类继承了基础实体 这里忽略id列
				if (rs.getString("COLUMN_NAME").equalsIgnoreCase("id")) {
					continue;
				}
				Map<String, String> column = Maps.newHashMap();
				column.put("colName", rs.getString("COLUMN_NAME"));
				column.put("name", colName2FieldName(rs.getString("COLUMN_NAME")));
				column.put("Name", StringUtils.capitalize(column.get("name")));
				column.put("type", sqlType2JavaType(rs.getString("TYPE_NAME")));
				column.put("colRemark", StringUtils.defaultString(rs.getString("REMARKS")));
				if (rs.getString("NULLABLE").equals(String.valueOf(DatabaseMetaData.columnNoNulls))) {
					column.put("notNull", "1");
				} else {
					column.put("notNull", "0");
				}
				columns.add(column);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return columns;
	}

	/**
	 * 将数据库类型转化为java类型
	 * 
	 * @param sqlType
	 * @return
	 */
	private static String sqlType2JavaType(String sqlType) {
		if (StringUtils.isBlank(sqlType)) {
			return "";
		}
		if (sqlType.equalsIgnoreCase("bit")) {
			return "Boolean";
		} else if (sqlType.equalsIgnoreCase("tinyint")) {
			return "Byte";
		} else if (sqlType.equalsIgnoreCase("smallint")) {
			return "Short";
		} else if (sqlType.equalsIgnoreCase("int")) {
			return "Integer";
		} else if (sqlType.equalsIgnoreCase("bigint")) {
			return "Long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			return "Float";
		} else if (sqlType.equalsIgnoreCase("decimal")
				|| sqlType.equalsIgnoreCase("numeric")
				|| sqlType.equalsIgnoreCase("double")
				|| sqlType.equalsIgnoreCase("real")
				|| sqlType.equalsIgnoreCase("money")
				|| sqlType.equalsIgnoreCase("smallmoney")) {
			return "Double";
		} else if (sqlType.equalsIgnoreCase("varchar")
				|| sqlType.equalsIgnoreCase("char")
				|| sqlType.equalsIgnoreCase("nvarchar")
				|| sqlType.equalsIgnoreCase("nchar")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("datetime")
				|| sqlType.equalsIgnoreCase("date")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("image")) {
			return "Blob";
		} else if (sqlType.equalsIgnoreCase("text")) {
			return "Clob";
		}
		return "";
	}

	/**
	 * 将列名转换为java驼峰式命名
	 * 
	 * @param colName
	 * @return
	 */
	private static String colName2FieldName(String colName) {
		if (StringUtils.isBlank(colName)) {
			return "";
		}
		char ch[] = colName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (i == 0) {
				ch[i] = Character.toLowerCase(ch[i]);
			}
			if ((i + 1) < ch.length) {
				if (ch[i] == '_') {
					ch[i + 1] = Character.toUpperCase(ch[i + 1]);
				} else {
					ch[i + 1] = Character.toLowerCase(ch[i + 1]);
				}
			}
		}
		return new String(ch).replace("_", "");
	}

}
