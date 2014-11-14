package com.github.tx.common.excel;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;

import com.github.tx.common.util.JodaTimeUtil;

public class DateConverter implements Converter {
	
	private String pattern;
	
	public DateConverter(){
	}
	
	public DateConverter(String pattern){
		this.pattern = pattern;
	}

	@Override
	public Object convert(Class arg0, Object arg1) {
		if (arg1 == null) {
            return null;
        }
		if(!StringUtils.isEmpty(this.pattern)){
			return JodaTimeUtil.convertFromString((String)arg1, this.pattern);
		}else{
			return JodaTimeUtil.convertFromString((String)arg1);
		}
	}

}
