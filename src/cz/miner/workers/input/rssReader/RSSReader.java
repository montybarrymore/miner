/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.workers.input.rssReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import cz.miner.system.Data;
import cz.miner.workers.Worker;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author daniel
 */
public class RSSReader extends Worker{
	private String url_;
	private String table_;
	private String column_;
	private List<String> newLinks_ = new ArrayList<>();
	private List<String> oldLinks_ = new ArrayList<>();	
	private List<String> regexes_ = new ArrayList<>();
	private String proxyAddress_ = "";
	private int proxyPort_ = 0;
	private String proxyName_ = "";
	private String proxyPassword_ = "";
	
	public RSSReader(String iniFile) throws FileNotFoundException, XMLStreamException, IOException, ParserConfigurationException, SAXException{
		File fXmlFile = new File(iniFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
			
		doc.getDocumentElement().normalize();

		url_ = doc.getElementsByTagName("url").item(0).getTextContent();
		table_ = doc.getElementsByTagName("table").item(0).getTextContent();
		column_ = doc.getElementsByTagName("text_column").item(0).getTextContent();
		
		NodeList nList = doc.getElementsByTagName("regex");
		for(int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			Element e = (Element)nNode;
			regexes_.add(e.getTextContent());
		}
				
		Node p = doc.getElementsByTagName("proxy").item(0);
		
		if(p == null) {
			proxyAddress_ = null;
		} else {
			Element pe = (Element)p;
			proxyAddress_ = pe.getElementsByTagName("address").item(0).getTextContent();
			String q = pe.getElementsByTagName("port").item(0).getTextContent();
			proxyPort_ = Integer.parseInt(q);
			proxyName_ = pe.getElementsByTagName("username").item(0).getTextContent();
			proxyPassword_ = pe.getElementsByTagName("password").item(0).getTextContent();			
		}
	}
	
	@Override
	public void doIt(Data data){
		data.addTable(table_);
		data.addColumn(table_, column_);
		boolean nextRead = true;
		do{
			try {
				readLinks();
			} catch (IOException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}
			
			if(newLinks_.size() > 0){
				nextRead = false;
			}else{
				try {
					System.out.println("Čekám");
					Thread.sleep(10000);
				} catch (InterruptedException ex) {
					Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} while(nextRead);
		
		String outLink = newLinks_.get(0);
		newLinks_.remove(0);
		oldLinks_.add(outLink);
		System.out.println("outLink: " + outLink);
		
		URL linkURL = null;
		try {
			linkURL = new URL(outLink);
		} catch (MalformedURLException ex) {
			Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
		}

		URLConnection conn = null;
		InputStream linkStream = null;
		
		if(proxyAddress_ == null) {
			try {
				linkStream = linkURL.openStream();
			} catch (IOException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			Authenticator.setDefault(authenticator);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress_, proxyPort_));

			try {
				conn = linkURL.openConnection(proxy);
			} catch (IOException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}			

			try {
				linkStream = conn.getInputStream();
			} catch (IOException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}
		}	
		
		ContentHandler contentHandler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		Parser parser = new AutoDetectParser();
		try {
			parser.parse(linkStream, contentHandler, metadata, new ParseContext());
		} catch (IOException | SAXException | TikaException ex) {
			Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		String outString = contentHandler.toString();
		outString = trimString(outString);
		outString = outString.replaceAll("\r\n", " ");

		data.addRow(table_);
		data.setValue(table_, column_, 0, outString);
		try {
			linkStream.close();
		} catch (IOException ex) {
			Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private boolean isNewLink(String link){
		boolean returnValue = true;
		for(String oldLink : oldLinks_){
			if(link.equals(oldLink)){
				returnValue = false;
			}
		}

		for(String newLink : newLinks_){
			if(link.equals(newLink)){
				returnValue = false;
			}
		}
		return returnValue;
	}
	
	private void readLinks() throws IOException{
		URL rssURL = null;
		try {
			rssURL = new URL(url_);
		} catch (MalformedURLException ex) {
			Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
		}

		URLConnection conn = null;
		InputStream rssStream = null;
		
		if(proxyAddress_ == null) {
			try {
				rssStream = rssURL.openStream();
			} catch (IOException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			Authenticator.setDefault(authenticator);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress_, proxyPort_));

			try {
				conn = rssURL.openConnection(proxy);
			} catch (IOException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}			

			try {
				rssStream = conn.getInputStream();
			} catch (IOException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		XMLInputFactory inputFactory = XMLInputFactory.newFactory();
		XMLEventReader eventReader = null;
		try {
			 eventReader = inputFactory.createXMLEventReader(rssStream);
		} catch (XMLStreamException ex) {
			Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		while (eventReader.hasNext()) {
			try {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					String localPart = event.asStartElement().getName().getLocalPart();
					switch (localPart) {
						case "link":
							event = eventReader.nextEvent();
							String link = event.asCharacters().getData();
							if(isNewLink(link)){
								newLinks_.add(link);
								System.out.println("Nový odkaz: " + link);
							}
							break;
					}
				}
			} catch (XMLStreamException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		rssStream.close();
	}
	
	private String trimString(String string){
		String returnValue = string;
		returnValue = returnValue.replaceAll("\t+", " ");
		returnValue = returnValue.replaceAll(" +", " ");
		returnValue = returnValue.replaceAll("\r\n ", "\r\n");		
		returnValue = returnValue.replaceAll("\r ", "\r\n");		
		returnValue = returnValue.replaceAll("\n ", "\r\n");
		returnValue = returnValue.replaceAll("[\r,\n]", "\r\n");
		returnValue = returnValue.replaceAll("(\r\n)+", "\r\n");
		returnValue = returnValue.replaceAll("\r\n", "---line terminator---");
		
		for(String re : regexes_){
			returnValue = returnValue.replaceAll(re, "");			
		}
		returnValue = returnValue.replaceAll("---line terminator---","\r\n");
		return returnValue;
	}
	
	Authenticator authenticator = new Authenticator() {
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(proxyName_, proxyPassword_.toCharArray()));
        }
	};
}