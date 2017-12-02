package cst.gu.util.poi.excel;

/**
 * @author guweichao 20170520
 * 记录了excel 每个Cell所在位置
 * 建议使用模板导出(指定excel文件作为模板,替换标记完成)
 */
@Deprecated
public class CellInfo {
	private String name ; // 名称
	private int firstRow ; // cell所占列
	private int lastRow ; // cell 行
	private int firstCol ; // cell所占列
	private int lastCol ; // cell 行
	
	/**
	 *  @author guweichao
	 *  20170607
	 *  非合并单元格
	 *
	 */
	public CellInfo(String name, int row,int col){
		this.name = name;
		this.firstRow = row;
		this.firstCol = col;
	}
	
	/**
	 *  @author guweichao
	 *  20170607
	 *  合并单元格
	 *
	 */
	public CellInfo(String name, int firstRow,int firstCol,int lastRow,int lastCol){
		this.name = name;
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.firstCol = firstCol;
		this.lastCol = lastCol;
	}
	public String getName() {
		return name;
	}
	public int getFirstRow() {
		return firstRow;
	}
	public int getLastRow() {
		return lastRow;
	}
	public int getFirstCol() {
		return firstCol;
	}
	public int getLastCol() {
		return lastCol;
	}
	

	
}
