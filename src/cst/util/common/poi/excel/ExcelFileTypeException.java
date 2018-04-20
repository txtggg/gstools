package cst.util.common.poi.excel;

import cst.util.common.file.FileTypeException;

public class ExcelFileTypeException extends FileTypeException{
	private static final long serialVersionUID = 1L;

	public ExcelFileTypeException(){
		super("文件格式错误,必须是.xls或xlsx文件");
	}
}
