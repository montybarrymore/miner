package cz.miner.workers.classification.regexClassifier;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 4.11.16.
 */
@XmlRootElement(name = "item")
public class ClassificationItem {
	@XmlElement(name = "category")
	private List<String> categories_ = new ArrayList<>();

	@XmlElement(name = "regex")
    private List<String> regexes_ = new ArrayList<>();

    public List<String> getCategories() {
        return categories_;
    }

    public List<String> getRegexes() {
        return regexes_;
    }
}
