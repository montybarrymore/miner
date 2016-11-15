package cz.miner.workers.classification.regexClassifier;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by daniel on 4.11.16.
 */
@XmlRootElement(name = "RegexClassifierConfig")
public class RegexClassifierConfig {
   ArrayList<ClassificationItem> knowledgebase = new ArrayList<>();
}