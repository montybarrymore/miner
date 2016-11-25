package cz.miner.workers.output.tcpDataWriter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by daniel on 25.11.16.
 */
@XmlRootElement(name = "tcpDataWriter")
public class TcpDataWriterConfig {
	public int socket;
}
