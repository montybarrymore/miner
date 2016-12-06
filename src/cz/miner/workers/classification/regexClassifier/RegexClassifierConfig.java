package cz.miner.workers.classification.regexClassifier;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

/**
 * Konfigurace pro RegexClassifier.
 * Created by daniel on 4.11.16.
 */
@XmlRootElement(name = "regexClassifierConfig")
@XmlSeeAlso({ClassificationItem.class})
public class RegexClassifierConfig {
	/**
	 * Seznam položek pro klasifikaci.
	 */
	@XmlElementWrapper(name = "knowledgebase")
	@XmlElement(name = "item")
	public List<ClassificationItem> knowledgeBase = new ArrayList<>();

	/**
	 * Název tabulky, ve které se nachází klasifikovaný text.
	 */
	public String inputTable;
	/**
	 * Název sloupce, ve kterém se nachází klasifikovaný text.
	 */
	public String inputColumn;
	/**
	 * Tabulka, do které jsou zapisovány výsledky klasifikace.
	 */
	public String outputTable;
	/**
	 * Sloupec pro zápis kategorií (taxonů).
	 */
	public String outputCategoryColumn;
	/**
	 * Sloupec pro zápis skore jednotlivých kategorií.
	 */
	public String outputScoreColumn;
}