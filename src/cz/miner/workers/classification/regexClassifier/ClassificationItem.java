package cz.miner.workers.classification.regexClassifier;

import java.util.ArrayList;

/**
 * Created by daniel on 4.11.16.
 */
public class ClassificationItem {
    private ArrayList<String> categories_ = new ArrayList<>();
    private ArrayList<String> regexes_ = new ArrayList<>();

    public void addCategory(String category) {
        categories_.add(category);
    }

    public ArrayList<String> getCategories() {
        return categories_;
    }

    public void addRegex(String regex) {
        regexes_.add(regex);
    }

    public ArrayList<String> getRegexes() {
        return regexes_;
    }
}
