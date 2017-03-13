/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.miner.workers.input.rssReader;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import cz.miner.system.Data;
import cz.miner.workers.Worker;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Načítá obsah RSS kanálu do datového streamu.
 * @version 1.0.1
 */
public class RSSReader extends Worker{
    /**
     * List obsahující nově přidané odkazy.
     */
    private List<String> newLinks_ = new ArrayList<>();

    /**
     * List obsahující již navštívené odkazy.
     */
    private List<String> oldLinks_ = new ArrayList<>();

    private RSSReaderConfig config_ = new RSSReaderConfig();

    /**
     * Vytvoří RSSReader.
     *
     * @param iniFile cesta ke konfiguračnímu souboru.
     * @throws IOException něco je špatně.
     * @throws ParserConfigurationException něco je špatně.
     * @throws SAXException něco je špatně.
     */
    public RSSReader(String iniFile) throws IOException, ParserConfigurationException, SAXException, TikaException, JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(RSSReaderConfig.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		config_ = (RSSReaderConfig) jaxbUnmarshaller.unmarshal(new File(iniFile));
	}

    /**
     * Načte data z RSS kanálu do streamu.
     * @param data datastream.
     */
	@Override
	public Data doIt(Data data){
		data.addTable(config_.table);
		data.addColumn(config_.table, config_.column);
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
					Thread.sleep(10000);
				} catch (InterruptedException ex) {
					Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} while(nextRead);
		
		String outLink = newLinks_.get(0);
		newLinks_.remove(0);
		oldLinks_.add(outLink);
		
		URL linkURL = null;
		try {
			linkURL = new URL(outLink);
		} catch (MalformedURLException ex) {
			Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
		}

		URLConnection conn = null;
		InputStream linkStream = null;
		
		if(config_.proxy == null) {
			try {
				linkStream = linkURL.openStream();
			} catch (IOException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			Authenticator.setDefault(authenticator);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config_.proxy.address, config_.proxy.port));

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

		data.addRow(config_.table);
		data.setValue(config_.table, config_.column, 0, outString);
		try {
			linkStream.close();
		} catch (IOException ex) {
			Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
		}

		return data;
	}

    /**
     * Otestuje odkaz, zda je nový.
     * @param link odkaz.
     * @return true, pokud je odkaz nový.
     */
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

    /**
     * Extrahuje odkazy z textu.
     * @throws IOException něco je špatně.
     */
    private void readLinks() throws IOException{
		URL rssURL = null;
		try {
			rssURL = new URL(config_.url);
		} catch (MalformedURLException ex) {
			Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
		}

		URLConnection conn = null;

		if(config_.proxy != null) {
			Authenticator.setDefault(authenticator);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config_.proxy.address, config_.proxy.port));

			try {
				conn = rssURL.openConnection(proxy);
			} catch (IOException ex) {
				Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
			}			
		} else {
			conn = rssURL.openConnection();
		}

		SyndFeedInput input = new SyndFeedInput();
        try {
            SyndFeed feed = input.build(new XmlReader(conn));

            for (SyndEntry entry : (List<SyndEntry>) feed.getEntries()) {
                String link = entry.getLink();
                if(link != null) {
                    if(isNewLink(link)){
                        newLinks_.add(link);
                    }

                }
            }
        } catch (FeedException e) {
            e.printStackTrace();
        }
	}

    /**
     * Odstraní konce řádků a ořízne text na základě regexes_.
     * @param string zpracovávaný text.
     * @return ořezaný text.
     */
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
		
		for(String re : config_.regexes){
			returnValue = returnValue.replaceAll(re, "");			
		}
		returnValue = returnValue.replaceAll("---line terminator---","\r\n");
		return returnValue;
	}

    /**
     * Autentizátor pro RSSProxy.
     */
	Authenticator authenticator = new Authenticator() {
        /**
         * Autentizace pro RSSProxy.
         * @return autentizace pro RSSProxy.
         */
        @Override
		public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(config_.proxy.username, config_.proxy.password.toCharArray()));
        }
	};
}
