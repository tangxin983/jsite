package com.github.tx.common.excel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.github.tx.common.util.ExcelUtil;

public class ImportExcel<T> {
	Class<T> clazz;

	public ImportExcel(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 将导入的excel转为List并返回
	 * @param in     输入流
	 * @param isValidateTitle 是否根据注解校验标题行
	 * @return 校验不通过返回null否则返回list
	 * @throws Exception
	 */
	public List<T> importExcel(InputStream in, boolean isValidateTitle) throws Exception {
		List<T> dist = new ArrayList<T>();
		// 将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中(key:标题,value:字段名)
		Map<String, String> fieldMap = getAnnotationFieldMap();
		// 第一页的所有行
		Iterator<Row> rows = ExcelUtil.getRows(in, 0);
		// 标题行
		Row title = rows.next();
		// 校验标题行
		if(isValidateTitle){
			if(!validateTitleRow(title, fieldMap)){
				return null;
			}
		}
		// 将标题的文字内容放入到一个map中(key:序号,value:标题)
		Map<Integer, String> titleMap = new HashMap<Integer, String>();
		int i = 0;
		for(Cell cell : title){
			String value = cell.getStringCellValue();
			titleMap.put(i, value);
			i++;
		}
		// 注册类型转化器
		ConvertUtils.register(new DateConverter(), Date.class);
		ConvertUtils.register(new BoolConverter(), Boolean.class);
		// 内容行开始
		while (rows.hasNext()) {
			Row row = rows.next();
			// 得到传入类的实例
			T t = clazz.newInstance();
			for(int k=0; k<fieldMap.keySet().size(); k++) {
			    // NULL列将作为空列  
				Cell cell = row.getCell(k, Row.CREATE_NULL_AS_BLANK);
				// 强制把列设置为string格式
				cell.setCellType(Cell.CELL_TYPE_STRING);
				// 这里得到此列的对应的标题
				String titleString = titleMap.get(k);
				// 如果这一列的标题和类中的某一列的Annotation相同，就进行设值
				if (fieldMap.containsKey(titleString)) {
					BeanUtils.setProperty(t, fieldMap.get(titleString),
							cell.getStringCellValue().trim());
				}
			}
			dist.add(t);
		}
		return dist;
	}

	/**
	 * 校验标题行是否符合预期
	 * 
	 * @param in
	 * @param columnCount
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private boolean validateTitleRow(Row title, Map<String, String> fieldMap)
			throws InvalidFormatException, IOException {
		// 检测标题行的列数
		if(title.getLastCellNum() != fieldMap.keySet().size()){
			return false;
		}
		// 检测标题行命名
		for (Cell cell : title) {
			String value = cell.getStringCellValue();
			if (StringUtils.isEmpty(value) || !fieldMap.keySet().contains(value)) {
				return false;
			}
		}
		return true;
	}

	private Map<String, String> getAnnotationFieldMap() {
		Field fields[] = clazz.getDeclaredFields();
		Map<String, String> fieldMap = new HashMap<String, String>();
		for (Field f : fields) {
			ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);
			if (exa != null) {
				fieldMap.put(exa.exportName(), f.getName());
			}
		}
		return fieldMap;
	}

}
