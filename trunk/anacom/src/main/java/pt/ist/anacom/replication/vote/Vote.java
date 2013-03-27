package pt.ist.anacom.replication.vote;


public class Vote {

    private int version = 0;
    private int counter = 0;
    private Object response = null;

    public Vote(int version, int counter, Object response) {
        this.version = version;
        this.counter = counter;
        this.response = response;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Vote) {
            Vote vote = (Vote) obj;
            return version == vote.getVersion();
        }
        return false;
    }

    public String toString() {
        return "Vote with version \"" + version + "\" counter \"" + counter + "\" and response " + response;
    }

}
