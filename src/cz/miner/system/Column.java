/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manufaktura.system;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daniel
 */
public class Column {
	public List<Cell> cells;
	private String name_;
	
	public Column(String name, int rowCount){
		name_ = name;
		cells = new ArrayList<>(rowCount);
	}
	
	public Object getValue(int rowNumber){
		Object returnValue = null;
		if(rowNumber < cells.size()){
			returnValue = cells.get(rowNumber);
		}
		return returnValue;
	}
	
	public void setValue(int rowNumber, Object value){
		cells.get(rowNumber).setValue(value);
	}
	
	public String getName(){
		return name_;
	}
}
