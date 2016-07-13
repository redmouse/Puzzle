/**
 * 
 */
package Puzzle;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author base
 *
 */
public class SudokuModel {
	
	/**
	 * 画面項目
	 */
	private String blockNum;
	
	private List<Map<String, String>> blockNumList = new ArrayList<Map<String, String>>();
	
	private String msg;
	
	
	/**
	 * ロジック用
	 */
	private Integer blockN = 0;
	private Integer lineN = 0;
	private Integer totalN = 0;
	List<Cell> sdkList = new ArrayList<Cell>();
	
	// 試し回数
	private Integer totalTestNums = 0;
	// recursionDeep
	private Integer recursionDeep = 0;
	// 値をセット回数
	private Integer totalSetNums = 0;
	
 	public SudokuModel(){
		Map<String, String> option1 = new HashMap<String, String>();
		Map<String, String> option2 = new HashMap<String, String>();
		Map<String, String> option3 = new HashMap<String, String>();
		option1.put("KEY", "3");
		option1.put("VALUE", "3");
		option2.put("KEY", "4");
		option2.put("VALUE", "4");
		option3.put("KEY", "5");
		option3.put("VALUE", "5");
		this.blockNumList.add(option1);
		this.blockNumList.add(option2);
		this.blockNumList.add(option3);
	}
	
	/**
	 * 初期化
	 */
	public boolean initCal(){
		this.blockN = Integer.parseInt(this.blockNum.toString());
		this.lineN = this.blockN * this.blockN;
		this.totalN = this.lineN * this.lineN;
		if (!chkInput()) {
			this.msg = "Input Failed!";
			return false;
		}
		initFontColor();
		initSdkListLink();
		initSdkListValSet();
//		printSdkList();
		return true;
	}

	
	// 各Cellの可能値リスト設定
	public void initSdkListValSet(){
		for (Cell cell : this.sdkList) {
			for (int k = 0; k < lineN; k++) {
				cell.getValSet().add(k+1);
			}
		}
		for (Cell cell : this.sdkList) {
			if (cell.getVal() != null && cell.getVal() > 0 ) {
				cell.getValSet().clear();
				cell.getValSet().add(cell.getVal());
				reduceByCell(sdkList, cell, blockN);
			}
		}
	}

	// 文字色の初期設定
	public void initFontColor(){
		for (Cell cell : this.sdkList) {
			if (cell.getVal() == null || cell.getVal() <=0) {
				cell.setColorCode("FF0000");
			}
		}
	}
	// 各Cellの関連Cellを設定
	public void initSdkListLink(){
		for (Cell curCell : this.sdkList) {
			for (Cell cell : this.sdkList) {
				if (curCell.getRow() == cell.getRow() && curCell.getCol() == cell.getCol()) {
					continue; // 自分を飛ばす
				}
				if (isSameRegion(curCell, cell, blockN)) {
					curCell.getRelateCells().add(cell);
					continue;
				}
				if (curCell.getRow() == cell.getRow() || curCell.getCol() == cell.getCol()) {
					curCell.getRelateCells().add(cell);
					continue;
				}
			}
		}
	}

	

	public void printSdkListBlock(List<Cell> sdkList){
		StringBuffer sb = new StringBuffer();
		sb.append("====================================================================\r\n");
		Integer curRow = 0;
		for (Cell cell : sdkList) {
			if (cell.getRow() != curRow) {
				sb.append("\r\n");
			}
			curRow = cell.getRow();
			sb.append(String.format("%1d", cell.getVal()).replaceAll("null", " ")).append(" ");
		}
		System.out.println(sb.toString());
	}
	
	public void printSdkList(List<Cell> sdkList){
		StringBuffer sb = new StringBuffer();
		sb.append("====================================================================\r\n");
		for (Cell cell : sdkList) {
			sb.append("行： ").append(String.format("% 3d", cell.getRow())).append("  ");
			sb.append("列： ").append(String.format("% 3d", cell.getCol())).append("  ");
			sb.append("値： ").append(String.format("% 2d", cell.getVal()).replace("null", "  ")).append("  ");
			sb.append("可能値： ");
			for (Integer velPos : cell.getValSet()) {
				sb.append(velPos).append(", ");
			}
			sb.append("\r\n");
		}
		System.out.println(sb.toString());
	}
	public void printUnconfirmedNums(List<Cell> sdkList){
		StringBuffer sb = new StringBuffer();
		sb.append("====================================================================\r\n");
		Integer num = 0;
		for (Cell cell : sdkList) {
			if (cell.getVal() != null && cell.getVal() > 0) {
				num ++;
			}
		}
		sb.append("未確定数： " + num);
		System.out.println(sb.toString());
	}

	public boolean recursion(List<Cell> sdkList){
		recursionDeep++;
//		System.out.println("recursionDeep: " + recursionDeep);
//		printUnconfirmedNums(sdkList);
//		printSdkList(sdkList);
		// clone
		List<Cell> sdkListChild = cloneSdkList(sdkList);
		// 最少可能性(2以上)のCellを探し出す。
		Cell cell = getLessPossCell(sdkListChild);
		Cell cellClone = cell.clone();
		
		for (Integer possVal : cellClone.getValSet()) {
			// 一回試し
			totalTestNums ++;
			System.out.println("totalTestNums: " + totalTestNums);
			cell.setVal(possVal);
			reducePossVal(sdkListChild);
			if (chkSuccess(sdkListChild)) {
				copySdkList(sdkList, sdkListChild);
				return true;
			}
			if(chkFailed(sdkListChild)){
				copySdkList(sdkListChild, sdkList);
				continue;
			}
			if (recursion(sdkListChild)) {
				copySdkList(sdkList, sdkListChild);
				return true;
			}
			else {
				recursionDeep--;
				copySdkList(sdkListChild, sdkList);
				continue;
			}
		}
		return false;
	}
	
	private void copySdkList(List<Cell> sdkList, List<Cell> sdkListCopyFrom){
		for (Cell cell : sdkList) {
			for (Cell cellCopyFrom : sdkListCopyFrom) {
				if (cell.getId() == cellCopyFrom.getId()) {
					copyCell(cell, cellCopyFrom);
				}
			}
		}
	}
	
	private void copyCell(Cell cell, Cell cellCopyFrom){
		cell.setVal(cellCopyFrom.getVal());
		cell.getValSet().clear();
		for (Integer val : cellCopyFrom.getValSet()) {
			cell.getValSet().add(val);
		}
	}
	
	private List<Cell> cloneSdkList(List<Cell> sdkList){
		List<Cell> cloned = new ArrayList<Cell>();
		for (Cell cell : sdkList) {
			cloned.add(cell.clone());
		}
		return cloned;
	}
	
	// 成功チェック
	private boolean chkSuccess(List<Cell> sdkList){
		for (Cell cell : sdkList) {
			if (cell.getValSet().size() != 1) {
				return false;
			}
		}
		return true;
	}
	// 失敗チェック(valSetが0になる)
	private boolean chkFailed(List<Cell> sdkList){
		for (Cell cell : sdkList) {
			if (cell.getValSet().size() == 0) {
				return true;
			}
		}
		return false;
	}
	
	// 最少可能性(2以上)のCellを探し出す。
	private Cell getLessPossCell(List<Cell> sdkList){
		Cell rtnCell = new Cell();
		for (Cell cell : sdkList) {
			if (cell.getValSet().size() > 1 && cell.getVal() == null) {
				if (rtnCell.getValSet().size() == 0 || rtnCell.getValSet().size() > cell.getValSet().size()) {
					rtnCell = cell;
				}
			}
		}
		return rtnCell;
	}
	
	// 唯一値計算（できないまで）
	public void reducePossVal(List<Cell> sdkList){
		while(reducePossValOnce(sdkList)){
			continue;
		}
	}
	// 唯一値計算（一回）
	public boolean reducePossValOnce(List<Cell> sdkList){
		boolean rtn = false;
		for (Cell cell : sdkList) {
			if (cell.getVal() != null && cell.getVal()>0) {
				rtn |= reduceByCell(sdkList,cell,blockN);
			}
		}
		return rtn;
	}
	
	// 固定されたCellにより、関連Cellの可能値を除去
	// true: 可能値除去あり
	public boolean reduceByCell(List<Cell> sdkList, Cell fixedCell, Integer blockN){
		boolean rtn = false;
		for (Cell cell : sdkList) {
			if (!isRelateCell(fixedCell, cell, blockN)) {
				continue;
			}
			if (!cell.getValSet().contains(fixedCell.getVal())) {
				continue;
			}
			rtn = true;
			cell.getValSet().remove(fixedCell.getVal());
			if (cell.getValSet().size() == 1) {
				for (Integer val : cell.getValSet()) {
					totalSetNums++;
					cell.setVal(val);
				}
			}
		}
		return rtn;
	}
	// 関連性あるかどうか
	public boolean isRelateCell(Cell c1, Cell c2, Integer blockN){
		// 同じCell
		if (c1.getRow() == c2.getRow() && c1.getCol() == c2.getCol()) {
			return false;
		}
		// 同じ区域
		if (c1.getRow()/blockN == c2.getRow()/blockN 
				&& c1.getCol()/blockN == c2.getCol()/blockN) {
			return true;
		}
		// 同じ行・列
		if (c1.getRow() == c2.getRow() || c1.getCol() == c2.getCol()) {
			return true;
		}
		return false;
	}

	// 同じ区域であるかどうかのチェック
	public boolean isSameRegion(Cell c1, Cell c2, Integer blockN){
		if (c1.getRow()/blockN == c2.getRow()/blockN 
				&& c1.getCol()/blockN == c2.getCol()/blockN) {
			return true;
		}
		return false;
	}

	
	public boolean chkInput(){
		for (Cell cell : this.sdkList) {
			if (cell.getVal() != null && cell.getVal() > lineN) {
				cell.setColorCode("FF0000");
				return false;
			}
		}
		return true;
	}
	
	//=================================================================
	// Getter & Setter
	//=================================================================	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(String blockNum) {
		this.blockNum = blockNum;
	}

	public List<Map<String, String>> getBlockNumList() {
		return blockNumList;
	}

	public void setBlockNumList(List<Map<String, String>> blockNumList) {
		this.blockNumList = blockNumList;
	}

	public List<Cell> getSdkList() {
		return sdkList;
	}

	public void setSdkList(List<Cell> sdkList) {
		this.sdkList = sdkList;
	}

	public Integer getTotalTestNums() {
		return totalTestNums;
	}

	public void setTotalTestNums(Integer totalTestNums) {
		this.totalTestNums = totalTestNums;
	}

	public Integer getRecursionDeep() {
		return recursionDeep;
	}

	public void setRecursionDeep(Integer recursionDeep) {
		this.recursionDeep = recursionDeep;
	}

	public Integer getTotalSetNums() {
		return totalSetNums;
	}

	public void setTotalSetNums(Integer totalSetNums) {
		this.totalSetNums = totalSetNums;
	}
	
	
}
