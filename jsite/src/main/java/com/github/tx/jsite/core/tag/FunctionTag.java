package com.github.tx.jsite.core.tag;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.tx.common.util.CollectionUtils;
import com.github.tx.jsite.modules.sys.entity.Dict;
import com.github.tx.jsite.modules.sys.entity.User;
import com.github.tx.jsite.modules.sys.service.DictService;
import com.github.tx.jsite.modules.sys.service.UserService;
import com.github.tx.jsite.utils.SpringContextHolder;

/**
 * 函数标签
 * 
 * @author tangx
 * 
 */
public class FunctionTag {
	
	private static UserService userService = SpringContextHolder.getBean(UserService.class);
	
	private static DictService dictService = SpringContextHolder.getBean(DictService.class);

	public static <T> boolean contains(Collection<T> coll, Object o) {
		if (coll != null && !coll.isEmpty()) {
			return coll.contains(o);
		} else {
			return false;
		}
	}

	public static <T> String join(List<T> list, String separator) {
		return StringUtils.join(list, separator);
	}
	
	public static <T> String extractProperty(Collection<T> collection, String propertyName, boolean ignoreEmptyValue) {
		return StringUtils.join(CollectionUtils.extractToList(collection, propertyName, ignoreEmptyValue), ",");
	}
	
	public static User getUserById(String userId) {
		if(StringUtils.isNotBlank(userId)) {
			return userService.selectById(userId);
		} else {
			return null;
		}
	}
	
	public static List<Dict> getDictList(String type){
		return dictService.findDictListByType(type);
	}
	
	public static String getDictLabel(String type, String value, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}
}
