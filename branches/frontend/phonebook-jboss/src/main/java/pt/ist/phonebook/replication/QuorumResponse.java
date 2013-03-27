package pt.ist.phonebook.replication;


public abstract class QuorumResponse {

    // Atributtes
    private Throwable exception = null;
    private String replicaURL = "";

    // Contructor
    public QuorumResponse(String replicaURL) {
        this.replicaURL = replicaURL;
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
