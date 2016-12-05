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
 * Created by daniel on 25.11.16.
 */
public class TcpDataWriter extends Worker{
	private TcpDataWriterConfig config_ = new TcpDataWriterConfig();
	private ServerSocket serverSocket_;

	public TcpDataWriter (String iniFile) throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(TcpDataWriterConfig.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		config_ = (TcpDataWriterConfig) jaxbUnmarshaller.unmarshal(new File(iniFile));

		serverSocket_= new ServerSocket(config_.socket);
	}

	@Override
	public void doIt(Data data) throws IOException{
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
				if(objectOutput != null) {
					objectOutput.close();
				}
				if(socket != null) {
					socket.close();
				}
				dataSendError = true;
			}
		}
	}
}
