package cz.miner.workers.output.tcpDataWriter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Konfigurace TcpDataWriteru.
 * Created by daniel on 25.11.16.
 */
@XmlRootElement(name = "tcpDataWriter")
public class TcpDataWriterConfig {
    /**
     * Císlo portu, na kterém bude naslouchat.
     */
    public int socket;
}
