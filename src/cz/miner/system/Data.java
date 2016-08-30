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
public class Data {
	private String name_;
	private List<Table> tables_;
	
	public Data(String name){
		name_ = name;
		tables_ = new ArrayList<>();
	}
	
	public void addTable(String name){
		tables_.add(new Table(name));
	}
	
	public void addColumn(String tableName, String columnName){
		for(Table table : tables_){
			if(table.getName().equals(tableName)){
				table.addColumn(columnName);
			}
		}
	}
	
	public void addRow(String tableName){
		for(Table table : tables_){
			if(table.getName().equals(tableName)){
				table.addRow();
			}
		}		
	}
	
	public void setValue(String tableName, String columnName, int rowNumber, Object value){
		for(Table table : tables_){
			if(table.getName().equals(tableName)){
				table.setValue(columnName, rowNumber, value);
			}			
		}
	}

	public Object getValue(String tableName, String columnName, int rowNumber){
		Object returnValue = null;
		for(Table t : tables_){
			if(t.getName().equals(tableName)){
				returnValue = t.getValue(columnName, rowNumber);
			}
		}
		return returnValue;
	}
	
	public int getRowCount(String table){
		int returnValue = 0;
		for(Table t : tables_){
			if(t.getName().equals(table)){
				returnValue = t.getRowCount();
			}
		}
		return returnValue;
	}
}
