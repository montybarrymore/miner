/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.system;

import cz.miner.workers.Worker;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Slouží k řízení jednotlivých workerů.
 * @version 1.0.0
 */
public class Factory {
    /**
     * Seznam workerů ve Factory.
     */
	private List<Worker> workers_;

    /**
     * Název streamu.
     */
    private String streamName_;

	/**
	 * Čas poslední modifikace konfiguračních souborů.
	 */
	private TreeMap<String, Long> mods_ = new TreeMap<>();

    /**
     * Vytvoří novou factory.
     * @param iniFile cesta ke konfiguračnímu souboru.
     * @throws IOException někde je chyba.
     * @throws XMLStreamException někde je chyba.
     * @throws ParserConfigurationException někde je chyba.
     * @throws SAXException někde je chyba.
	 * @throws JAXBException někde je chyba.
     */
	public Factory(String iniFile) throws IOException, XMLStreamException, ParserConfigurationException, SAXException, JAXBException, TikaException, IllegalAccessException, InvocationTargetException {
	    BufferedReader br = new BufferedReader(new FileReader(iniFile));
	    mods_.put(iniFile, new File(iniFile).lastModified());
		streamName_ = br.readLine();
		workers_ = new ArrayList<>();
		while(br.ready()){
			String line = br.readLine();
			String workerType = line.substring(0, line.indexOf(" "));
			line = line.substring(line.indexOf(" "));
			line = line.trim();
			String workerName = line.substring(0, line.indexOf(" "));
			line = line.substring(line.indexOf(" "));
			line = line.trim();
			String workerIniFile = line;
			System.out.println("Nový pracovník:");
			System.out.print("Funkce: ");
			System.out.println(workerType);			
			System.out.print("Jméno:  ");
			System.out.println(workerName);
			System.out.print("Soubor: ");
			System.out.println(workerIniFile);

			String className = "cz.miner.workers." + workerType;
            Constructor c = null;
            Worker worker = null;

            try {
                c = Class.forName(className).getConstructor(String.class);
                worker = (Worker) c.newInstance(workerIniFile);
                worker.setName(workerName);
                workers_.add(worker);
                mods_.put(workerIniFile, new File(workerIniFile).lastModified());
            } catch (Exception e) {
                System.out.println("Worker nevytvoren.");
            }
		}
	}

    /**
     * Zahájí zpracování streamu.
	 * @throws IOException někde je chyba.
	 * @throws InterruptedException někde je chyba.
	 */
    public void work() throws IOException, InterruptedException {
        Data data = new Data(streamName_);
        for(int i = 0; i < workers_.size(); i++){
            data = workers_.get(i).doIt(data);
        }
	}

	public boolean isModified() {
        boolean returnValue = false;
        for(Map.Entry<String, Long> entry : mods_.entrySet()) {
            if(new File(entry.getKey()).lastModified() != entry.getValue()) {
                returnValue = true;
            }
        }
        return returnValue;
    }
}
