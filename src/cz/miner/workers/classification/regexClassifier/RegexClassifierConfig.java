package cz.miner.workers.classification.regexClassifier;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 4.11.16.
 */
@XmlRootElement(name = "regexClassifierConfig")
@XmlSeeAlso({ClassificationItem.class})
public class RegexClassifierConfig {
	@XmlElementWrapper(name = "knowledgebase")
	@XmlElement(name = "item")
	public List<ClassificationItem> knowledgeBase = new ArrayList<ClassificationItem>();

	public String inputTable;
	public String inputColumn;
	public String outputTable;
	public String outputCategoryColumn;
	public String outputScoreColumn;
}