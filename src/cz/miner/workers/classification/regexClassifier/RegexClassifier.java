package cz.miner.workers.classification.regexClassifier;

import cz.miner.workers.Worker;

import javax.xml.bind.JAXBException;

/**
 * Created by daniel on 27.10.16.
 */
public class  RegexClassifier extends Worker {
	private RegexClassifierConfig config_ = new RegexClassifierConfig();

	public RegexClassifier(String iniFile) throws JAXBException {
/*		File file = new File(iniFile);
		JAXBContext jaxbContext = JAXBContext.newInstance(RegexClassifierConfig.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		config_ = (RegexClassifierConfig) jaxbUnmarshaller.unmarshal(file);*/

		config_.knowledgebase.add(new ClassificationItem());
		System.out.println(config_);
		System.out.println();
	}
}
