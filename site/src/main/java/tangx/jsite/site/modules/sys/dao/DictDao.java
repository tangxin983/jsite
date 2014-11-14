package tangx.jsite.site.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import tangx.jsite.site.core.persistence.annotation.MyBatisDao;
import tangx.jsite.site.core.persistence.dao.BaseDao;
import tangx.jsite.site.modules.sys.entity.Dict;

/**
 * 字典Dao
 * @author tangx
 * @since 2014-06-25
 */
@MyBatisDao
public interface DictDao extends BaseDao<Dict> {
	
	@Select("select type from sys_dict group by type")
	public List<String> findTypeList();
	
}
