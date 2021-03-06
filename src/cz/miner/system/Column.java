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
 * Slouží k uložení buněk třídy Cell.
 */
public class Column implements Serializable {
	/**
	 * Seznam buněk.
	 */
	private List<Cell> cells_;

	/**
	 * Jméno sloupce.
	 */
	private String name_;

	/**
	 * Konstruktor sloupce. Vytvoří nový sloupec se zadaným jménem a počtem řádků.
	 * @param name	jméno sloupce.
	 * @param rowCount	počet řádků.
	 */
	public Column(String name, int rowCount){
		name_ = name;
		cells_ = new ArrayList<>(rowCount);
	}

	/**
	 * Vrátí hodnotu buňky.
	 * @param rowNumber	číslo řádku.
	 * @return	hodnota buňky.
	 */
	public Object getValue(int rowNumber){
		Object returnValue = null;
		if(rowNumber < cells_.size()){
			returnValue = cells_.get(rowNumber).getValue();
		}
		return returnValue;
	}

	/**
     * Nastaví hodnotu buňky.
     * @param rowNumber číslo řádku.
     * @param value hodnota buňky.
     */
	public void setValue(int rowNumber, Object value){
		cells_.get(rowNumber).setValue(value);
	}

    /**
     * Vrátí název sloupce.
     * @return  název sloupce.
     */
	public String getName(){
		return name_;
	}

    /**
     * Přidá prázdnou buňku na konec sloupce.
     */
	public void addEmptyCell() {
		cells_.add(new Cell());
	}
}
