package pt.ist.anacom.replication.quorumresponse;

import pt.ist.anacom.replication.CommandStrategy;

public abstract class QuorumResponse {

    // Atributtes
    private final CommandStrategy command;
    private Throwable exception = null;
    private String replicaURL = "";

    // Contructor
    public QuorumResponse(String replicaURL, CommandStrategy command) {
        this.replicaURL = replicaURL;
        this.command = command;
    }

    public CommandStrategy getCommand() {
        return command;
    }

    public Throwable getException() {
        return exception;
    }

    public String getReplicaURL() {
        return replicaURL;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
