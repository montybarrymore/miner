/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.system;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daniel
 */
public class Table {
	private String name_;
	private int rowCount_;
	public List<Column> columns;
	
	public Table(String name){
		name_ = name;
		rowCount_ = 0;
		columns = new ArrayList<>();
	}
	
	public void addColumn(String name){
		columns.add(new Column(name, rowCount_));
	}
	
	public String getName(){
		return name_;
	}
	
	public void addRow(){
		for(Column column : columns){
			column.addEmptyCell();
		}
		rowCount_++;
	}
	
	public void setValue(String columnName, int rowNumber, Object value){
		for(Column column : columns){
			if(column.getName().equals(columnName)){
				column.setValue(rowNumber, value);
			}
		}
	}
	
	public Object getValue(String columnName, int rowNumber){
		Object returnValue = null;
		for(Column column : columns){
			if(column.getName().equals(columnName)){
				returnValue = column.getValue(rowNumber);
			}
		}
		return returnValue;
	}
	
	public int getRowCount(){
		return rowCount_;
	}
}
