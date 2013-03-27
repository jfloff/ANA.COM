package pt.ist.anacom.shared.exception;

import java.util.ArrayList;

public class ReplicasDownProtocolException extends AnacomException {

    private static final long serialVersionUID = 1L;
    
    private ArrayList<String> urlList;

    public ReplicasDownProtocolException() {
    }

    public ReplicasDownProtocolException(ArrayList<String> urlList) {
        super("Number of replicas down: " + urlList.size() + ". Should be at most one.");
        
        this.urlList = urlList;
    }

    public ArrayList<String> getUrlList() {
        return urlList;
    }
    
}
