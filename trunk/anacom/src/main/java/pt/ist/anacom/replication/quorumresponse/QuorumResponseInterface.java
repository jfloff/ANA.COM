package pt.ist.anacom.replication.quorumresponse;

import pt.ist.anacom.replication.CommandStrategy;

public interface QuorumResponseInterface {


    public CommandStrategy getCommand();

    public Throwable getException();

    public String getReplicaURL();

    public void setException(Throwable exception);
    
}
