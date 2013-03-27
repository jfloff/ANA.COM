package pt.ist.anacom.replication;

import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.registry.BusinessQueryManager;

public abstract class WriteCommandStrategy extends CommandStrategy {

    private final ConcurrentHashMap<String, Integer> responseList;

    public WriteCommandStrategy(String prefix, BusinessQueryManager businessQueryManager) throws MalformedURLException {
        super(prefix, businessQueryManager);
        responseList = new ConcurrentHashMap<String, Integer>();

    }

    @Override
    public void execute() {
        System.out.println("-------  SEND TO ALL REPLICAS ------- ");
        sendToReplicas();
        System.out.println("---------- WAITING QUORUM ------- ");
        waitQuorum();
        System.out.println("------- CHECK FOR EXCEPTIONS ------- ");
        checkExceptions();
    }

    @Override
    public void addResponse(String url, Object responseType) {
        responseList.put(url, (Integer) responseType);
    }

}
