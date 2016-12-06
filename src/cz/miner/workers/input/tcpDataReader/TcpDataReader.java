package cz.miner.workers.input.tcpDataReader;

import cz.miner.system.Data;
import cz.miner.workers.Worker;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.time.LocalTime;

import static java.lang.Thread.sleep;

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
        boolean waitingForData = true;
        while(waitingForData) {
            if(serverIndex >= config_.servers.size()) {
                serverIndex = 0;
            }

            String serverString = config_.servers.get(serverIndex);
            String serverName = serverString.substring(0, serverString.indexOf(":"));
            int serverPort = Integer.parseInt(serverString.substring(serverString.indexOf(":") + 1));

            Socket socket = null;
            ObjectInputStream inputStream = null;
            try {
                socket = new Socket(serverName, serverPort);
                socket.setSoTimeout(config_.timeout);
                inputStream = new ObjectInputStream(socket.getInputStream());
                data = (Data) inputStream.readObject();
                waitingForData = false;
                System.out.println(LocalTime.now().toString() + " " +  data.getName());
                inputStream.close();
                socket.close();
            } catch(Exception e) {
                System.out.println(e.getMessage());
                sleep(config_.timeout);
                serverIndex++;
                if(inputStream != null) {
                    inputStream.close();
                }
                if(socket != null) {
                    socket.close();
                }
            }
        }

        return data;
    }
}
