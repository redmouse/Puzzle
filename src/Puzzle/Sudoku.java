package Puzzle;

import java.util.Date;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Servlet implementation class PuzzleMain
 */
public class Sudoku extends ActionSupport{
	private static final long serialVersionUID = 1L;
    
	public SudokuModel model = new SudokuModel();
	
	public String ENTER_KEY = "<br/>";
	
	public String Calculate(){
		long startTime = System.currentTimeMillis();
		if (!model.initCal()) {
			return "success";
		}
		model.printSdkList(model.getSdkList());
		model.recursion(model.getSdkList());
		long endTime = System.currentTimeMillis();
		String msg = "";
		msg += "試し回数： " + model.getTotalTestNums()+ENTER_KEY;
		msg += "セット回数： " + model.getTotalSetNums()+ENTER_KEY;
		msg += "Deeps： " + model.getRecursionDeep()+ENTER_KEY;
		msg += "Times： " + (endTime - startTime) + " ms" +ENTER_KEY;
		model.setMsg(msg);
		return "success";
	}
	
	public String ReduceOnce(){
		model.initCal();
		return "success";
	}

	public String Display(){
		return "success";
	}
	
	
	public SudokuModel getModel() {
		return model;
	}

	public void setModel(SudokuModel model) {
		this.model = model;
	}
	
}
