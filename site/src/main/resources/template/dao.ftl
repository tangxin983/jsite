package ${packageName}.${moduleName}.dao${subModuleName};

import tangx.jsite.site.core.persistence.annotation.MyBatisDao;
import tangx.jsite.site.core.persistence.dao.BaseDao;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};

/**
 * ${functionName}Dao
 * @author ${classAuthor}
 * @since ${classVersion}
 */
@MyBatisDao
public interface ${ClassName}Dao extends BaseDao<${ClassName}> {
	
}
