/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.system;

import cz.miner.workers.Tester;
import cz.miner.workers.Worker;
import cz.miner.workers.input.rssReader.RSSReader;
import cz.miner.workers.output.terminalWriter.TerminalWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daniel
 */
public class Factory {
	private List<Worker> workers_;
	private String streamName_;
	public Factory(String iniFile) throws FileNotFoundException, IOException, XMLStreamException, ParserConfigurationException, SAXException{
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
			
			if(workerType.equals("input.RSSReader")){
				Worker worker = new RSSReader(workerIniFile);
				worker.setName(workerName);
				workers_.add(worker);
			}

			if(workerType.equals("output.TerminalWriter")){
				Worker worker = new TerminalWriter(workerIniFile);
				worker.setName(workerName);
				workers_.add(worker);
			}
		}
	}
	
	public void work(){
		while(true){
			Data data = new Data(streamName_);
			for(int i = 0; i < workers_.size(); i++){
				workers_.get(i).doIt(data);
			}
		}
	}
}
