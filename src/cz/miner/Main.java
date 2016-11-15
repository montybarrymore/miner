/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner;

import cz.miner.system.Factory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * 
 * @author daniel
 */ 
public class Main {
    /**
     * Tady to všechno začíná.
     * @param args cesta ke konfiguračnímu souboru.
     * @throws IOException někde je chyba.
     * @throws XMLStreamException někde je chyba.
     * @throws ParserConfigurationException někde je chyba.
     * @throws SAXException někde je chyba.
     */
	public static void main(String[] args) throws IOException, XMLStreamException, ParserConfigurationException, SAXException, JAXBException {
		String iniFile = args[0];
		Factory factory = new Factory(iniFile);
		factory.work();
	}
}
