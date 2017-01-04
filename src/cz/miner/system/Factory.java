/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.system;

import cz.miner.workers.Tester;
import cz.miner.workers.Worker;
import cz.miner.workers.classification.regexClassifier.RegexClassifier;
import cz.miner.workers.input.rssReader.RSSReader;
import cz.miner.workers.input.tcpDataReader.TcpDataReader;
import cz.miner.workers.output.tcpDataWriter.TcpDataWriter;
import cz.miner.workers.output.terminalWriter.TerminalWriter;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * Vytvoří novou factory.
     * @param iniFile cesta ke konfiguračnímu souboru.
     * @throws IOException někde je chyba.
     * @throws XMLStreamException někde je chyba.
     * @throws ParserConfigurationException někde je chyba.
     * @throws SAXException někde je chyba.
	 * @throws JAXBException někde je chyba.
     */
	public Factory(String iniFile) throws IOException, XMLStreamException, ParserConfigurationException, SAXException, JAXBException {
		BufferedReader br = new BufferedReader(new FileReader(iniFile));
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

			if(workerType.equals("Tester")){
				Worker worker = new Tester(workerIniFile);
				worker.setName(workerName);
				workers_.add(worker);
			}

			if(workerType.equals("classification.RegexClassifier")){
				Worker worker = new RegexClassifier(workerIniFile);
				worker.setName(workerName);
				workers_.add(worker);
			}

			if(workerType.equals("input.RSSReader")){
				Worker worker = new RSSReader(workerIniFile);
				worker.setName(workerName);
				workers_.add(worker);
			}

			if(workerType.equals("input.TcpDataReader")){
				Worker worker = new TcpDataReader(workerIniFile);
				worker.setName(workerName);
				workers_.add(worker);
			}

			if(workerType.equals("output.TcpDataWriter")){
				Worker worker = new TcpDataWriter(workerIniFile);
				worker.setName(workerName);
				workers_.add(worker);
			}

			if(workerType.equals("output.TerminalWriter")) {
				Worker worker = new TerminalWriter(workerIniFile);
				worker.setName(workerName);
				workers_.add(worker);
			}
		}
	}

    /**
     * Zahájí zpracování streamu.
	 * @throws IOException někde je chyba.
	 * @throws InterruptedException někde je chyba.
	 */
    public void work() throws IOException, InterruptedException {
		while(true){
			Data data = new Data(streamName_);
			for(int i = 0; i < workers_.size(); i++){
				data = workers_.get(i).doIt(data);
			}
		}
	}
}
