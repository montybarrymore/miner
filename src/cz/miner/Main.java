/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner;

import cz.miner.system.Factory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * @author daniel
 */ 
public class Main {
	/**
	 * 
	 * @param args cesta k ini souboru
	 */
	public static void main(String[] args) throws IOException, FileNotFoundException, XMLStreamException, ParserConfigurationException, SAXException {
		String iniFile = args[0];
		Factory factory = new Factory(iniFile);
		factory.work();
	}
}
