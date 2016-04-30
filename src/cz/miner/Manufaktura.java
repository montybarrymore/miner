/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manufaktura;

import java.io.FileNotFoundException;
import manufaktura.system.Factory;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/**
 * 
 * @author daniel
 */ 
public class Manufaktura {
	/**
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException, FileNotFoundException, XMLStreamException, ParserConfigurationException, SAXException {
		String iniFile = args[0];
		Factory factory = new Factory(iniFile);
		factory.work();
	}
}
