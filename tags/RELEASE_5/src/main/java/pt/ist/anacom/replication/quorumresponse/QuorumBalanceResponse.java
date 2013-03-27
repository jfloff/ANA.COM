package pt.ist.anacom.replication.quorumresponse;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.CommandStrategy;
import pt.ist.anacom.shared.stubs.client.BalanceType;

public class QuorumBalanceResponse extends QuorumResponse implements AsyncHandler<BalanceType> {

    public QuorumBalanceResponse(String replicaURL, CommandStrategy command) {
        super(replicaURL, command);
    }

    @Override
    public void handleResponse(Response<BalanceType> res) {

        try {
            BalanceType replicaResponse = res.get();

            Logger.getLogger(this.getClass()).info(">>>> [LOG] QuorumBalanceResponse BALANCE: " + replicaResponse.getBalance()
                    + " REPLICA VERSION AFTER: " + replicaResponse.getVersion());
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
