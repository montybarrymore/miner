#!/bin/bash
java -jar miner.jar input/BBCInput-proxy.ini &
java -jar miner.jar input/iDnesInput-proxy.ini &
java -jar miner.jar input/TASSInput-proxy.ini &
java -jar miner.jar input/PalbaInput-proxy.ini &
