package pt.ist.anacom.replication.quorumresponse;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.CommandStrategy;
import pt.ist.anacom.shared.stubs.client.SMSPhoneReceivedListType;

public class QuorumSMSPhoneReceivedListResponse extends QuorumResponse implements AsyncHandler<SMSPhoneReceivedListType> {


    public QuorumSMSPhoneReceivedListResponse(String replicaURL, CommandStrategy commandStrategy) {
        super(replicaURL, commandStrategy);
    }

    @Override
    public void handleResponse(Response<SMSPhoneReceivedListType> res) {

        try {
            SMSPhoneReceivedListType replicaResponse = res.get();

            Logger.getLogger(this.getClass()).info(">>>> [LOG]REPLICA VERSION AFTER: " + replicaResponse.getVersion());
            super.getCommand().addResponse(super.getReplicaURL(), replicaResponse);

        } catch (InterruptedException e) {
            Logger.getLogger(this.getClass()).info("Throwing original exception type is " + e.getCause().getClass().getName());
            super.setException(e);
        } catch (ExecutionException e) {
            super.setException(e);
        } finally {
            CommandStrategy.quorumCounter.incrementAndGet();
        }
    }
}
