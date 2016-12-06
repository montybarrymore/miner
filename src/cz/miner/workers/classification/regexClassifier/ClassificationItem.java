package cz.miner.workers.classification.regexClassifier;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída obsahující výrazy a taxony pro jednu entitu.
 */
@XmlRootElement(name = "item")
public class ClassificationItem {
    /**
     * Seznam kategorií (taxonů).
     */
    @XmlElement(name = "category")
	private List<String> categories_ = new ArrayList<>();

    /**
     * Seznam regulárních výrazů.
     */
    @XmlElement(name = "regex")
    private List<String> regexes_ = new ArrayList<>();

    /**
     * Vrátí seznam kategorií.
     * @return seznam kategorií.
     */
    public List<String> getCategories() {
        return categories_;
    }

    /**
     * Vrátí seznam regulárních výrazů.
     * @return seznam regulárních výrazů.
     */
    public List<String> getRegexes() {
        return regexes_;
    }
}
