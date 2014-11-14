package com.github.tx.common.excel;

import org.apache.commons.beanutils.Converter;

public class BoolConverter implements Converter {

	@Override
	public Object convert(Class arg0, Object arg1) {
		Boolean bool = false;
		String value = arg1.toString();
        if(value.equals("Y") || value.equals("是")){
            bool = true;
        }
        return bool;
	}

}
