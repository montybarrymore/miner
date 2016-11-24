package cz.miner.workers.classification.regexClassifier;

import cz.miner.system.Data;
import cz.miner.workers.Worker;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by daniel on 27.10.16.
 */
public class  RegexClassifier extends Worker {
	private RegexClassifierConfig config_ = new RegexClassifierConfig();

	public RegexClassifier(String iniFile) throws JAXBException {
		File file = new File(iniFile);
		JAXBContext jaxbContext = JAXBContext.newInstance(RegexClassifierConfig.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		config_ = (RegexClassifierConfig) jaxbUnmarshaller.unmarshal(file);
	}

	@Override
	public void doIt(Data data) {
		String text = "";
		for(int i = 0; i < data.getRowCount(config_.inputTable); i++) {
			text += (String) data.getValue(config_.inputTable, config_.inputColumn, i);
		}


	}
}
