package cz.miner.workers.input.rssReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

/**
 * Konfigurace pro RSSReader
 * @version 0.0.0
 *          Created by daniel on 13.3.17.
 */
@XmlRootElement(name = "RSSReaderConfig")
@XmlSeeAlso({RSSProxy.class})
public class RSSReaderConfig {
    @XmlElement(name = "RSSProxy")
    public RSSProxy proxy;

    /**
     * Adresa rss streamu.
     */
    public String url;

    /**
     * Jméno tabulky, do které se načítá text z RSS.
     */
    public String table;

    /**
     * Jméno sloupce, do kerého se načítá tex z RSS.
     */
    public String column;

    /**
     * Seznam vyrazu pro vymazani nezadoucich casti textu.
     */
    @XmlElementWrapper(name = "regexes")
    @XmlElement(name = "regex")
    public List<String> regexes = new ArrayList<>();
}

