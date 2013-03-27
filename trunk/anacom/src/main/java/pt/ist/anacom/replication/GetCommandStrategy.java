package pt.ist.anacom.replication;

import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.quorumresponse.QuorumResponse;
import pt.ist.anacom.replication.vote.VoteList;
import pt.ist.anacom.shared.exception.AnacomException;

public abstract class GetCommandStrategy extends CommandStrategy {

    protected ArrayList<? extends QuorumResponse> handlerList;
    protected VoteList voteList;
    protected Object responseType;

    public GetCommandStrategy(String prefix, BusinessQueryManager businessQueryManager) throws MalformedURLException {
        super(prefix, businessQueryManager);
    }

    @Override
    public void execute() {
        Logger.getLogger(this.getClass()).info("-------  SEND TO ALL REPLICAS ------- ");
        sendToReplicas();
        Logger.getLogger(this.getClass()).info("---------- WAITING QUORUM ------- ");
        waitQuorum();
        Logger.getLogger(this.getClass()).info("------- CHECK FOR EXCEPTIONS ------- ");
        checkExceptions();
        Logger.getLogger(this.getClass()).info("------- VOTE ------- ");
        vote();
        Logger.getLogger(this.getClass()).info("------- RETURN ------- ");
        getResponse();
    }

    public abstract void vote();

    public abstract void getExtraResponse() throws AnacomException;

    public void getResponse() {

        // Get the best response based on Voting
        responseType = voteList.getBestResponse(getQuorumLength());

        Logger.getLogger(this.getClass()).info("[QUORUM] Response after bestResponse -> " + responseType);

        // Return best vote or find again with more replicas
        if (responseType == null) {

            // If we have all replicas
            if (quorumCounter.get() == getReplicaLength()) {

                Logger.getLogger(this.getClass()).info("[QUORUM] Got all responses from all servers");
                responseType = voteList.getBestResponse(getQuorumLength());

            } else {

                getExtraResponse();

            }
        }
    }

}
