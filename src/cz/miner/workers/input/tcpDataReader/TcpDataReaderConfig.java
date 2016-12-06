package cz.miner.workers.input.tcpDataReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Konfigurace pro TcpDataReader
 * Created by daniel on 5.12.16.
 */
@XmlRootElement(name = "tcpDataReaderConfig")
public class TcpDataReaderConfig {
    /**
     * Seznam serverů ve formátu "IP:port"
     */
    @XmlElementWrapper(name = "servers")
    @XmlElement(name = "server")
    public List<String> servers = new ArrayList<>();

    /**
     * Timeout pro načítání datastreamů.
     */
    public int timeout;
}
