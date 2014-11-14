package ${packageName}.${moduleName}.dao${subModuleName};

import com.github.tx.jsite.core.persistence.annotation.MyBatisDao;
import com.github.tx.jsite.core.persistence.dao.BaseDao;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};

/**
 * ${functionName}Dao
 * @author ${classAuthor}
 * @since ${classVersion}
 */
@MyBatisDao
public interface ${ClassName}Dao extends BaseDao<${ClassName}> {
	
}
