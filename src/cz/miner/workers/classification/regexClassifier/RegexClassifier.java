package cz.miner.workers.classification.regexClassifier;

import cz.miner.system.Data;
import cz.miner.workers.Worker;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

/**
 * Created by daniel on 27.10.16.
 */
public class  RegexClassifier extends Worker {
	private RegexClassifierConfig config_ = new RegexClassifierConfig();

	public RegexClassifier(String iniFile) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(RegexClassifierConfig.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		config_ = (RegexClassifierConfig) jaxbUnmarshaller.unmarshal(new File(iniFile));
	}

	@Override
	public Data doIt(Data data) {
		String text = "";
		for(int i = 0; i < data.getRowCount(config_.inputTable); i++) {
			text += (String) data.getValue(config_.inputTable, config_.inputColumn, i);
		}

		TreeMap<String, Integer> result = new TreeMap<String, Integer>();
		for(ClassificationItem classificationItem : config_.knowledgeBase) {
			ArrayList<String> categories = (ArrayList) classificationItem.getCategories();
			ArrayList<String> regexes = (ArrayList) classificationItem.getRegexes();

			for(String regex : regexes) {
				if(text.matches("(.*)" + regex + "(.*)")) {
					for(String category : categories) {
						if(result.containsKey(category)) {
							int count = result.get(category);
							count++;
							result.put(category, count);
						} else  {
							result.put(category, 1);
						}
					}
				}
			}
		}

		data.addTable(config_.outputTable);
		data.addColumn(config_.outputTable, config_.outputCategoryColumn);
		data.addColumn(config_.outputTable, config_.outputScoreColumn);
		for(Map.Entry<String,Integer> entry : result.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			data.addRow(config_.outputTable);
			data.setValue(config_.outputTable, config_.outputCategoryColumn, data.getRowCount(config_.outputTable) - 1, key);
			data.setValue(config_.outputTable, config_.outputScoreColumn, data.getRowCount(config_.outputTable) - 1, value);
		}

		return data;
	}
}
