/**
 * 
 */
package Puzzle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author base
 *
 */
public class Cell {

	private Integer val;
	
	private Integer id;
	
	// 色設定
	private String colorCode;
	
	// 0~8
	private Integer row;
	private Integer col;
	
	// 可能な値リスト
	private Set<Integer> valSet = new HashSet<Integer>();
	
	// 関連するCell(同行、列、区域)
	private List<Cell> relateCells = new ArrayList<Cell>();

	
	public Cell clone(){
		Cell cell = new Cell();
		cell.id = this.id;
		cell.val = this.val;
		cell.row = this.row;
		cell.col = this.col;
		cell.valSet = new HashSet<Integer>();
		for (Integer intVal : this.valSet) {
			cell.valSet.add(intVal);
		}
		return cell;
	}
	
	//=================================================================
	// Getter & Setter
	//=================================================================


	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getCol() {
		return col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}

	public List<Cell> getRelateCells() {
		return relateCells;
	}

	public void setRelateCells(List<Cell> relateCells) {
		this.relateCells = relateCells;
	}

	public Integer getVal() {
		return val;
	}

	public void setVal(Integer val) {
		if (val == null || val == 0) {
			val = null;
		}
		this.val = val;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Integer> getValSet() {
		return valSet;
	}

	public void setValSet(Set<Integer> valSet) {
		this.valSet = valSet;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
}
