/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.workers;

import cz.miner.system.Data;

import java.io.IOException;

/**
 * Dělník ve Factory.
 */
public class Worker {
	/**
	 * Jméno workera.
	 */
	private String name_;

	/**
	 * Zpracuje data.
	 * @param data zpracovávaný datastream.
	 */
	public void doIt(Data data) throws IOException {
		System.out.println(name_ + ": doIt nedefinovano");
	}

	/**
	 * Mastaví jméno workera.
	 * @param name jméno workera.
     */
	public void setName(String name){
		name_ = name;
	}
}
