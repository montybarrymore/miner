package cz.miner.workers.output.tcpDataWriter;

import cz.miner.system.Data;
import cz.miner.workers.Worker;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
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
	 * Konstruktor.
	 * @param iniFile Cesta ke konfiguračnímu souboru.
	 * @throws JAXBException chyba v konfiguračím souboru.
	 * @throws IOException chyba při odesílání dat.
	 */
	public TcpDataWriter (String iniFile) throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(TcpDataWriterConfig.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		config_ = (TcpDataWriterConfig) jaxbUnmarshaller.unmarshal(new File(iniFile));
	}

	@Override
	public Data doIt(Data data) throws IOException{
		while (true) {
			boolean success = false;

			do {
				ServerSocket serverSocket = null;
				Socket socket = null;
				ObjectOutputStream outputStream = null;
				ObjectInputStream inputStream = null;

				boolean clientConnected = false;
				boolean clientReadyToSend = true;
				boolean objectSent = false;

				do {
					try {
						serverSocket = new ServerSocket(config_.socket);
						socket = serverSocket.accept();
						outputStream = new ObjectOutputStream(socket.getOutputStream());
						inputStream = new ObjectInputStream(socket.getInputStream());
						clientConnected = true;
					} catch (Exception e) {
						System.out.println(e.getMessage());
						if(serverSocket != null) {
							serverSocket.close();
						}
					}
				} while (!clientConnected);

				try {
					String s = (String) inputStream.readObject();
					if (s.equals("Ready to receive.")) {
						clientReadyToSend = true;
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

				if (clientReadyToSend) {
					try {
						outputStream.writeObject(data);
						outputStream.flush();
						objectSent = true;
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}

				if (objectSent) {
					try {
						String s = (String) inputStream.readObject();
						if (s.equals("Object received.")) {
							success = true;
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}

				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
				if (serverSocket != null) {
					serverSocket.close();
				}
				if (socket != null) {
					socket.close();
				}

			} while (!success);

			return data;
		}
	}
}
