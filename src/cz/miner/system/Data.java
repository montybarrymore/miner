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
	public List<Table> tables;
	
	public Data(String name){
		name_ = name;
		tables = new ArrayList<>();
	}
	
	public void addTable(String name){
		tables.add(new Table(name));
	}
	
	public void addColumn(String tableName, String columnName){
		for(Table table : tables){
			if(table.getName().equals(tableName)){
				table.addColumn(columnName);
			}
		}
	}
	
	public void addRow(String tableName){
		for(Table table : tables){
			if(table.getName().equals(tableName)){
				table.addRow();
			}
		}		
	}
	
	public void setValue(String tableName, String columnName, int rowNumer, Object value){
		for(Table table : tables){
			if(table.getName().equals(tableName)){
				table.setValue(columnName, rowNumer, value);
			}			
		}
	}
	
	public Object getValue(String tableName, String columnName, int rowNumer){
		Object returnValue = null;
		for(Table t : tables){
			if(t.getName().equals(tableName)){
				returnValue = t.getValue(columnName, rowNumer);
			}
		}
		return returnValue;
	}
	
	public int getRowCount(String table){
		int returnValue = 0;
		for(Table t : tables){
			if(t.getName().equals(table)){
				returnValue = t.getRowCount();
			}
		}
		return returnValue;
	}
}
