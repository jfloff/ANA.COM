package pt.ist.anacom.replication;

import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

public abstract class WriteCommandStrategy extends CommandStrategy {

    private final ConcurrentHashMap<String, Integer> responseList;

    public WriteCommandStrategy(String prefix, BusinessQueryManager businessQueryManager) throws MalformedURLException {
        super(prefix, businessQueryManager);
        responseList = new ConcurrentHashMap<String, Integer>();

    }

    @Override
    public void execute() {
        Logger.getLogger(this.getClass()).info("-------  SEND TO ALL REPLICAS ------- ");
        sendToReplicas();
        Logger.getLogger(this.getClass()).info("---------- WAITING QUORUM ------- ");
        waitQuorum();
        Logger.getLogger(this.getClass()).info("------- CHECK FOR EXCEPTIONS ------- ");
        checkExceptions();
    }

    @Override
    public void addResponse(String url, Object responseType) {
        responseList.put(url, (Integer) responseType);
    }

}
