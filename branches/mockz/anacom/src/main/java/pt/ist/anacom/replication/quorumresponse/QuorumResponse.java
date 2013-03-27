package pt.ist.anacom.replication.quorumresponse;

import pt.ist.anacom.replication.CommandStrategy;

public abstract class QuorumResponse implements QuorumResponseInterface {

    // Atributtes
    private final CommandStrategy command;
    private Throwable exception = null;
    private String replicaURL = "";

    // Contructor
    public QuorumResponse(String replicaURL, CommandStrategy command) {
        this.replicaURL = replicaURL;
        this.command = command;
    }

    @Override
    public CommandStrategy getCommand() {
        return command;
    }

    @Override
    public Throwable getException() {
        return exception;
    }

    @Override
    public String getReplicaURL() {
        return replicaURL;
    }

    @Override
    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
