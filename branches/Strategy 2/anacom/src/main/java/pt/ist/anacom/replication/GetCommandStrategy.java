package pt.ist.anacom.replication;

import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.registry.BusinessQueryManager;

import pt.ist.anacom.replication.quorumresponse.QuorumResponse;

public abstract class GetCommandStrategy extends CommandStrategy {

    protected ArrayList<? extends QuorumResponse> handlerList;

    public GetCommandStrategy(String prefix, BusinessQueryManager businessQueryManager) throws MalformedURLException {
        super(prefix, businessQueryManager);
    }
    
    public abstract void vote();
    
    public abstract void getResponse();

    @Override
    public void execute() {
        System.out.println("-------  SEND TO ALL REPLICAS ------- ");
        sendToReplicas();
        System.out.println("---------- WAITING QUORUM ------- ");
        waitQuorum();
        System.out.println("------- CHECK FOR EXCEPTIONS ------- ");
        checkExceptions();
        System.out.println("------- VOTE ------- ");
        vote();
        System.out.println("------- RETURN ------- "); 
        getResponse();
    }

}