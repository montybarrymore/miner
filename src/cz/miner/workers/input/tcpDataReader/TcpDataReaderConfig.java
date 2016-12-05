package cz.miner.workers.input.tcpDataReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 5.12.16.
 */
@XmlRootElement(name = "tcpDataReaderConfig")
public class TcpDataReaderConfig {
    @XmlElementWrapper(name = "servers")
    @XmlElement(name = "server")
    public List<String> servers = new ArrayList<>();

    public int timeout;
}
