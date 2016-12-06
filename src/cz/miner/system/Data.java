/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Slouží k předávání dat mezi jendotlivými workery ve Factory.
 * @author daniel
 */
public class Data implements Serializable {
	/**
	 * Jméno datasetu.
	 */
	private String name_;

    /**
     * List obsahující jednotlivé tabulky v datasetu.
     */
	private List<Table> tables_;

    /**
     * Vytvoří nový prázdný dataset.
     * @param name jméno nového datasetu.
     */
    public Data(String name){
		name_ = name;
		tables_ = new ArrayList<>();
	}

    /**
     * Přidá do datasetu tabulku.
     * @param tableName jméno přidávané tabulky.
     */
    public void addTable(String tableName){
		tables_.add(new Table(tableName));
	}

    /**
     * Přidá nový sloupec do tabulky.
     * @param tableName jméno tabulky.
     * @param columnName jméno sloupce.
     */
    public void addColumn(String tableName, String columnName){
		for(Table table : tables_){
			if(table.getName().equals(tableName)){
				table.addColumn(columnName);
			}
		}
	}

    /**
     * Přidá řádek na konec tabulky.
     * @param tableName jméno tabulky.
     */
    public void addRow(String tableName){
		for(Table table : tables_){
			if(table.getName().equals(tableName)){
				table.addRow();
			}
		}		
	}

    /**
     * Nastaví hodnotu buňky.
     * @param tableName jméno tabulky.
     * @param columnName jméno sloupce.
     * @param rowNumber číslo řádku.
     * @param value hodnota.
     */
    public void setValue(String tableName, String columnName, int rowNumber, Object value){
		for(Table table : tables_){
			if(table.getName().equals(tableName)){
				table.setValue(columnName, rowNumber, value);
			}			
		}
	}

    /**
     * Vrátí hodnotu buňky.
     * @param tableName jméno tabulky.
     * @param columnName jméno sloupce.
     * @param rowNumber číslo řádku.
     * @return hodnota buňky.
     */
    public Object getValue(String tableName, String columnName, int rowNumber){
		Object returnValue = null;
		for(Table table : tables_){
			if(table.getName().equals(tableName)){
				returnValue = table.getValue(columnName, rowNumber);
			}
		}
		return returnValue;
	}

    /**
     * Vrátí počet řádků tabulky.
     * @param tableName jméno tabulky.
     * @return počet řádků.
     */
    public int getRowCount(String tableName){
		int returnValue = 0;
		for(Table table : tables_){
			if(table.getName().equals(tableName)){
				returnValue = table.getRowCount();
			}
		}
		return returnValue;
	}

    /**
     * Vrátí jméno datastreamu
     * @return jméno streamu.
     */
    public String getName() {
    	return name_;
	}
}
