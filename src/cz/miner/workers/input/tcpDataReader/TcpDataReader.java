package cz.miner.workers.input.tcpDataReader;

import cz.miner.system.Data;
import cz.miner.workers.Worker;

import java.io.ObjectInputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Created by daniel on 25.11.16.
 */
public class TcpDataReader extends Worker{
    private TcpDataReaderConfig config_ = new TcpDataReaderConfig();

    public TcpDataReader(String iniFile) {
    }

    public void doIt(Data data) throws InterruptedException {
        try{
            Socket socket = new Socket("127.0.0.1", 10000);
            socket.setSoTimeout(1000);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            data = (Data) inputStream.readObject();
            System.out.println(data.toString());
            inputStream.close();
            socket.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            sleep(10000);
        }
    }
}
