package cz.miner.workers.input.tcpDataReader;

import cz.miner.system.Data;
import cz.miner.workers.Worker;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Načítá datastreamy ze zadaných IP adres a portů. Nejprve vyčte všechny streamy z jednoho zdroje, po uplynutí timeoutu
 * čte z dalšího.
 * Created by daniel on 25.11.16.
 */
public class TcpDataReader extends Worker{
    /**
     * Konfigurace workeru.
     */
    private TcpDataReaderConfig config_ = new TcpDataReaderConfig();
    /**
     * Index právě obsluhovaného zdroje.
     */
    private int serverIndex = 0;

    /**
     * Konstruktor.
     * @param iniFile Cesta k souboru s konfigurací.
     * @throws JAXBException chyba v konfigračním souboru.
     */
    public TcpDataReader(String iniFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TcpDataReaderConfig.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        config_ = (TcpDataReaderConfig) jaxbUnmarshaller.unmarshal(new File(iniFile));
    }

    public Data doIt(Data data) throws IOException, InterruptedException {
        Socket clientSocket = null;

        boolean connected = false;
        boolean objectReceived = false;

        int i = 0;
        do {
            if (i >= config_.servers.size()) {
                i = 0;
                Thread.sleep(config_.timeout);
            }

            try {
                String server = config_.servers.get(i);
                String serverName = server.substring(0, server.indexOf(":"));
                int serverPort = Integer.parseInt(server.substring(server.indexOf(":") + 1));
                clientSocket = new Socket(serverName, serverPort);
                connected = true;
            } catch (Exception e) {
//                System.out.println(config_.servers.get(i).toString() + " " + e.getMessage());
                i++;
            }
        } while (!connected);

        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

        outputStream.writeObject("Ready to receive.");
        outputStream.flush();

        try {
            data = (Data) inputStream.readObject();
            objectReceived = true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        if(objectReceived) {
            outputStream.writeObject("Object received.");
            outputStream.flush();
        }

        outputStream.close();
        inputStream.close();
        clientSocket.close();

        return data;
    }
}
