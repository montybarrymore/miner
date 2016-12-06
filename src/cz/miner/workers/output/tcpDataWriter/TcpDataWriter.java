package cz.miner.workers.output.tcpDataWriter;

import cz.miner.system.Data;
import cz.miner.workers.Worker;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Odesílá datastream. Vytvoří soket na zadaném portu a čeká na TcpDataReader, až si je vyzvedne.
 * Created by daniel on 25.11.16.
 */
public class TcpDataWriter extends Worker{
	/**
	 * Konfigurace TcpDataWriteru.
	 */
	private TcpDataWriterConfig config_ = new TcpDataWriterConfig();
	/**
	 * Socket, na kterém čeká na odebrání dat.
	 */
	private ServerSocket serverSocket_;

	/**
	 * Konstruktor.
	 * @param iniFile Cesta ke konfiguračnímu souboru.
	 * @throws JAXBException chyba v konfiguračím souboru.
	 * @throws IOException chyba při odesílání dat.
	 */
	public TcpDataWriter (String iniFile) throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(TcpDataWriterConfig.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		config_ = (TcpDataWriterConfig) jaxbUnmarshaller.unmarshal(new File(iniFile));

		serverSocket_= new ServerSocket(config_.socket);
	}

	@Override
	public Data doIt(Data data) throws IOException{
		Socket socket = null;
		ObjectOutputStream objectOutput = null;

		boolean dataSendError = true;
		while(dataSendError) {
			System.out.println("server");
			try {
				socket = serverSocket_.accept();
				objectOutput = new ObjectOutputStream(socket.getOutputStream());
				objectOutput.writeObject(data);
				objectOutput.close();
				socket.close();
				dataSendError = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				try {
					if(objectOutput != null) {
						objectOutput.close();
					}
				} catch(Exception e1) {
					System.out.println(e1.getMessage());
				}
				try {
					if(socket != null) {
						socket.close();
					}
				} catch(Exception e1) {
					System.out.println(e1.getMessage());
				}
				dataSendError = true;
			}
		}

		return data;
	}
}
