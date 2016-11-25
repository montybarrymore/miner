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
 * Obsahuje data uspořádaná do sloupců a řádků.
 */
public class Table implements Serializable {
	/**
	 * Jméno tabulky.
	 */
	private String name_;

	/**
	 * Počet řádků v tabulce.
	 */
	private int rowCount_;

    /**
     * list obsahující sloupce tabulky.
     */
    private List<Column> columns_;

    /**
     * Vytvoří novou tabulku.
     * @param name jméno tabulky.
     */
    public Table(String name){
		name_ = name;
		rowCount_ = 0;
		columns_ = new ArrayList<>();
	}

    /**
     * Přídá sloupec do tabulky.
     * @param name jméno sloupce.
     */
    public void addColumn(String name){
		columns_.add(new Column(name, rowCount_));
	}

    /**
     * Vrátí jméno tabulky.
     * @return jméno tabulky.
     */
    public String getName(){
		return name_;
	}

    /**
     * Přidá řádek ke všem sloupcům v tabulce.
     */
    public void addRow(){
		for(Column column : columns_){
			column.addEmptyCell();
		}
		rowCount_++;
	}

    /**
     * Nastaví hodnotu buňky.
     * @param columnName jméno sloupce.
     * @param rowNumber číslo řádku.
     * @param value hodnota buňky.
     */
	public void setValue(String columnName, int rowNumber, Object value){
		for(Column column : columns_){
			if(column.getName().equals(columnName)){
				column.setValue(rowNumber, value);
			}
		}
	}

    /**
     * Vrátí hodnotu buňky.
     * @param columnName jméno sloupce.
     * @param rowNumber číslo řádku.
     * @return hodnota buňky.
     */
    public Object getValue(String columnName, int rowNumber){
		Object returnValue = null;
		for(Column column : columns_){
			if(column.getName().equals(columnName)){
				returnValue = column.getValue(rowNumber);
			}
		}
		return returnValue;
	}

    /**
     * Vrátí počet řádků tabulky.
     * @return počet řádků.
     */
	public int getRowCount(){
		return rowCount_;
	}
}
