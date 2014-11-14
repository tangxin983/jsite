package ${packageName}.${moduleName}.service${subModuleName};

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.tx.jsite.core.web.service.BaseService;
import com.github.tx.jsite.core.persistence.entity.PageEntity;
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
	<#if isPagination>
	@Override
	public PageEntity<${ClassName}> selectByPage(HttpServletRequest request) {
		return super.selectByPage(request);
	}
	<#else>
	@Override
	public List<${ClassName}> select(HttpServletRequest request) {
		return super.select(request);
	}
	</#if>
	
	@Override
	public int insert(${ClassName} entity) {
		return super.insert(entity);
	}
	
	@Override
	public int updateById(${ClassName} entity) {
		return super.updateById(entity);
	}

	@Override
	public int deleteById(String id) {
		return super.deleteById(id);
	}
	 
	@Override
	public void deleteByIds(List<String> ids) {
		super.deleteByIds(ids);
	}
	
}
