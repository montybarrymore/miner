/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.system;

/**
 * Slouží k uložení dat. Může obsahovat libovolný objekt.
 */
public class Cell {
	/**
	 * Hodnota uložená v buňce.
	 */
	private Object value_;

	/**
	 * Vrátí hodnotu uloženou v buňce.
	 * @return	Uložená hodnota.
	 */
	public Object getValue(){
		return value_;
	}

	/**
	 * Nastaví hodnotu buňky. V případě, že je již hodnota nastavena, je stará hodnota přepsána.
	 * @param value    Nová hodnota buňky.
	 */
	public void setValue(Object value){
		value_ = value;
	}
}
