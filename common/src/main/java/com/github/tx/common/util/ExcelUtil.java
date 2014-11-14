package com.github.tx.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {

	/**
	 * 获得sheet
	 * @param in 输入流
	 * @param i  第几页(从0开始)
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Sheet getSheet(InputStream in, int i) throws InvalidFormatException, IOException {
		Workbook book = WorkbookFactory.create(in);
		return book.getSheetAt(i);
	}
	
	/**
	 * 获得sheet
	 * @param file excel文件
	 * @param i    第几页(从0开始)
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Sheet getSheet(File file, int i) throws InvalidFormatException, IOException {
		FileInputStream in = new FileInputStream(file);
		return getSheet(in, i);
	}
	
	/**
	 * 获得某页所有行
	 * @param in 输入流
	 * @param i  第几页(从0开始)
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Iterator<Row> getRows(InputStream in, int i) throws InvalidFormatException, IOException {
		Sheet sheet = getSheet(in, i);
		return sheet.rowIterator();
	}
	
	/**
	 * 获得某页所有行
	 * @param in 输入流
	 * @param i  第几页(从0开始)
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Iterator<Row> getRows(File file, int i) throws InvalidFormatException, IOException {
		Sheet sheet = getSheet(file, i);
		return sheet.rowIterator();
	}
}
