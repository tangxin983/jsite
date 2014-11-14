package ${packageName}.${moduleName}.service${subModuleName};

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tangx.jsite.site.core.web.service.BaseService;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};
import ${packageName}.${moduleName}.dao${subModuleName}.${ClassName}Dao;

/**
 * ${functionName}Service
 * @author ${classAuthor}
 * @since ${classVersion}
 */
@Service
@Transactional
public class ${ClassName}Service extends BaseService<${ClassName}> {

	private ${ClassName}Dao ${className}Dao;

	@Autowired
	public void set${ClassName}Dao(${ClassName}Dao ${className}Dao) {
		super.setDao(${className}Dao);
		this.${className}Dao = ${className}Dao;
	}
	
	// ========== 以下为简单增删改示例。 修改以适应实际需求===========
	@Override
	public void insert(${ClassName} entity) {
		super.insert(entity);
	}
	
	@Override
	public void update(${ClassName} entity) {
		super.update(entity);
	}

	@Override
	public void delete(String id) {
		super.delete(id);
	}
	 
	@Override
	public void delete(List<String> ids) {
		super.delete(ids);
	}
	
}
