/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.system;

import java.util.ArrayList;
import java.util.List;

/**
 * Slouží k uložení buněk třídy Cell.
 */
public class Column {
	/**
	 * Seznam buněk.
	 */
	private List<Cell> cells;

	/**
	 * Jméno sloupce.
	 */
	private String name_;
	
	public Column(String name, int rowCount){
		name_ = name;
		cells = new ArrayList<>(rowCount);
	}
	
	public Object getValue(int rowNumber){
		Object returnValue = null;
		if(rowNumber < cells.size()){
			returnValue = cells.get(rowNumber).getValue();
		}
		return returnValue;
	}
	
	public void setValue(int rowNumber, Object value){
		cells.get(rowNumber).setValue(value);
	}
	
	public String getName(){
		return name_;
	}

	public void addEmptyCell() {
		cells.add(new Cell());
	}
}
