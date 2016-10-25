/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.workers;

/**
 * Testovací worker. Nic nedělá.
 */
public class Tester extends Worker{
	/**
	 * Vytvoří nový Tester a vypíše iniFile.
	 * @param iniFile cesta ke konfiguračnímu souboru.
	 */
	public Tester(String iniFile){
		System.out.println(iniFile);
	}
}
