package com.github.tx.common.excel;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.github.tx.common.util.JodaTimeUtil;
 
public class ExportExcel<T> {

	/**
	 * 导出excel
	 * @param title   sheet标题
	 * @param dataSet 数据集
	 * @param out     输出流
	 */
	public void exportExcel(String title, List<T> dataSet, OutputStream out) {
		try {
			if(CollectionUtils.isEmpty(dataSet)){
				throw new Exception("记录集为空！");
			}
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(title);
			// 设置表格默认列宽度为15个字节
			sheet.setDefaultColumnWidth(15);
			// 生成一个样式
			HSSFCellStyle style = workbook.createCellStyle();
			// 设置标题样式
			style = ExcelStyle.setHeadStyle(workbook, style);
		 
			// 泛型class对象
			Class<?> cls = Class.forName(dataSet.get(0).getClass().getName());
			// 所有字段
			Field fields[] = cls.getDeclaredFields();
			// 标题list
			List<String> titleList = new ArrayList<String>();
			for(Field f : fields){
				// 有annottion的字段才 添加到标题list
				ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);
				if (exa != null) {
					titleList.add(exa.exportName());
				}
			}
			// 表格标题行
			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < titleList.size(); i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(titleList.get(i));
				cell.setCellValue(text);
			}
			// 表格数据行开始(标题行为0)
			int index = 1;
			for(T t : dataSet){
				row = sheet.createRow(index);
				for (int i = 0; i < fields.length; i++) {
					HSSFCell cell = row.createCell(i);
					String textValue = getValue(PropertyUtils.getProperty(t, fields[i].getName()));
					cell.setCellValue(textValue);
				}
				index++;
			}
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getValue(Object value) {
		String textValue = "";
		if (value == null)
			return textValue;
		if (value instanceof Boolean) {
			boolean bValue = (Boolean) value;
			textValue = "是";
			if (!bValue) {
				textValue = "否";
			}
		} else if (value instanceof Date) {
			textValue = JodaTimeUtil.convertToString((Date)value);
		} else {
			textValue = value.toString();
		}
		return textValue;
	}

}
