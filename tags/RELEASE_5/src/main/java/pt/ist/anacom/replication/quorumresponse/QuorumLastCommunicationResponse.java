package pt.ist.anacom.replication.quorumresponse;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.CommandStrategy;
import pt.ist.anacom.shared.stubs.client.LastCommunicationType;

public class QuorumLastCommunicationResponse extends QuorumResponse implements AsyncHandler<LastCommunicationType> {


    public QuorumLastCommunicationResponse(String replicaURL, CommandStrategy commandStrategy) {
        super(replicaURL, commandStrategy);
    }

    @Override
    public void handleResponse(Response<LastCommunicationType> res) {

        try {
            LastCommunicationType replicaResponse = res.get();

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
