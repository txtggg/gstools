package cst.gu.util.poi.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * @author guweichao 20170520 
 *  建议使用模板导出(指定excel文件作为模板,替换标记完成)
 */
@Deprecated
public class CellStyleInfo {
	protected CellStyle cellstyle;
	protected List<CellInfo> cells = new ArrayList<CellInfo>();
	protected List<int[]> widths = new ArrayList<int[]>();
	protected Map<Integer,Short> heights = new HashMap<Integer,Short>();
	public CellStyleInfo(CellStyle cellstyle) {
		this.cellstyle = cellstyle;
	}

	public CellStyleInfo addCellWidth(int col,int width){
		int[] widthi = {col,width};
		widths.add(widthi);
		return this;
	}
	
	/**
	 * @author guweichao 20170606
	 * @param row 行号,从0开始
	 * @param height 参数实际是short 为了书写方便,这里使用int参数然后强转,传入参数需要注意不要溢出
	 * @return this,方便进行链式add
	 */
	public CellStyleInfo addCellHeight(int row,int height){
		heights.put(row, (short)height);
		return this;
	}
	
	public CellStyleInfo addCellInfo(CellInfo cellInfo) {
		if(cellInfo != null){
			cells.add(cellInfo);
		}
		return this;
	}

	/**
	 * 普通单元格:非合并
	 * @param content 单元格内容
	 * @param row 行号(从0开始)
	 * @param col 列号(从0开始)
	 * @return
	 */
	public CellStyleInfo addCellInfo(String content, int row,int col) {
		cells.add(new CellInfo(content, row, col));
		return this;
	}
	
	/**
	 * 合并单元格
	 * @param content 单元格内容
	 * @param firstRow 合并单元格的第一行(含)
	 * @param firstCol 合并单元格的第一列(含)
	 * @param lastRow 合并单元格的最后一行(含)
	 * @param lastCol 合并单元格的最后一列(含)
	 * @return
	 */
	public CellStyleInfo addCellInfo(String content, int firstRow,int firstCol,int lastRow,int lastCol) {
		cells.add(new CellInfo(content, firstRow, firstCol,lastRow,lastCol));
		return this;
	}
}
