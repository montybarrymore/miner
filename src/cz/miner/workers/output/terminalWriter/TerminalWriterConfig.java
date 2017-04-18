package cz.miner.workers.output.terminalWriter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Konfigurace TerminalWriteru
 * @version 1.0.0
 *          Created by daniel on 31.3.17.
 */
@XmlRootElement(name = "terminalWriter")
public class TerminalWriterConfig {
    public String table;

}
