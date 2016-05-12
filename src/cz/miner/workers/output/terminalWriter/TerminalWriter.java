/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.workers.output.terminalWriter;

import cz.miner.system.Data;
import cz.miner.workers.Worker;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daniel
 */
public class TerminalWriter extends Worker{
    /**
     * Jméno tabulky, ze které se vypisují sloupce na výstup.
     */
    private String table_;

    /**
     * Seznam sloupců, které se budou vypisovat na výstup.
     */
    private List<OutputColumn> outputColumns_;

	public TerminalWriter(String iniFile) throws XMLStreamException, IOException{
		outputColumns_ = new ArrayList<>();
		
		XMLInputFactory inputFactory = XMLInputFactory.newFactory();
		InputStream inputStream = new FileInputStream(iniFile);
		XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
		while(eventReader.hasNext()){
			boolean continueReading = true;
			XMLEvent event = eventReader.nextEvent();
			if(event.isStartElement()){
				String localPart = event.asStartElement().getName().getLocalPart();
				switch(localPart){
					case "table":
						event = eventReader.nextEvent();
						table_ = event.asCharacters().getData();
						break;
					case "column":
						OutputColumn outputColumn = new OutputColumn();
						do{
							event = eventReader.nextEvent();
							continueReading = true;
							if(event.isEndElement()){
								if(event.asEndElement().getName().getLocalPart().equals("column")){
									continueReading = false;
								}
							}
							if(event.isStartElement()){
								String columnPart = event.asStartElement().getName().getLocalPart();
								switch(columnPart){
									case "name":
										event = eventReader.nextEvent();
										outputColumn.name = event.asCharacters().getData();
										break;
									case "width":
										event = eventReader.nextEvent();
										outputColumn.width = Integer.parseInt(event.asCharacters().getData());
										break;
								}
							}
						}while(continueReading);
						outputColumns_.add(outputColumn);	
				}
			}
		}
		inputStream.close();
	}
	
	@Override
    public void doIt(Data data){
		// Vrchni cara v tabulce
		System.out.print("+");
		for(OutputColumn outColumn : outputColumns_){
			for(int i = 0; i < outColumn.width; i++){
				System.out.print("-");
			}
			System.out.print("+");
		}
		System.out.println("");
		
		// Jmena sloupcu
		System.out.print("|");
		for(OutputColumn outColumn : outputColumns_){
			String columnName = outColumn.name;
			while(columnName.length() < outColumn.width){
				columnName = columnName + " ";
			}
			
			System.out.print(columnName);
			System.out.print("|");
		}
		System.out.println("");
		
		// Cara pod jmeny sloupcu
		System.out.print("+");
		for(OutputColumn outColumn : outputColumns_){
			for(int i = 0; i < outColumn.width; i++){
				System.out.print("-");
			}
			System.out.print("+");
		}
		System.out.println("");	
		
		// Vypis obsahu bunek
		for(int i = 0; i < data.getRowCount(table_); i++){
			ArrayList<String> cells = new ArrayList<>();
			for(OutputColumn outColumn : outputColumns_){
				String cell = (String) data.getValue(table_, outColumn.name, i);
				cells.add(cell);
			}
			
			int sumLength = 0;
			for(String c : cells){
				sumLength = sumLength + c.length();
			}
			
			while(sumLength > 0){
				for(int col = 0; col < outputColumns_.size(); col++){
					System.out.print("|");
					String s = cells.get(col);
					if(s.length() < outputColumns_.get(col).width){
						while(s.length() < outputColumns_.get(col).width){
							s = s + " ";
						}
						cells.set(col, "");
					}else{
						s = s.substring(0, outputColumns_.get(col).width);
						String newCell = cells.get(col).substring(outputColumns_.get(col).width);
						cells.set(col, newCell);
					}
					System.out.print(s);
					System.out.print("|");
				}
				System.out.println("");

				sumLength = 0;
				for(String c : cells){
					sumLength = sumLength + c.length();
				}
			}
		}

		// Cara na konci tabulky
		System.out.print("+");
		for(OutputColumn outColumn : outputColumns_){
			for(int i = 0; i < outColumn.width; i++){
				System.out.print("-");
			}
			System.out.print("+");
		}
		System.out.println("");	
	}
}
