package pt.ist.anacom.replication.quorumresponse;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import pt.ist.anacom.replication.CommandStrategy;
import pt.ist.anacom.shared.stubs.client.BalanceAndPhoneListType;

public class QuorumBalanceAndPhoneListResponse extends QuorumResponse implements AsyncHandler<BalanceAndPhoneListType> {


    public QuorumBalanceAndPhoneListResponse(String replicaURL, CommandStrategy commandStrategy) {
        super(replicaURL, commandStrategy);
    }

    @Override
    public void handleResponse(Response<BalanceAndPhoneListType> res) {

        try {
            BalanceAndPhoneListType replicaResponse = res.get();

            System.out.println(">>>> [LOG]REPLICA VERSION AFTER: " + replicaResponse.getVersion());
            super.getCommand().addResponse(super.getReplicaURL(), replicaResponse);

        } catch (InterruptedException e) {
            System.out.println("Throwing original exception type is " + e.getCause().getClass().getName());
            super.setException(e);
        } catch (ExecutionException e) {
            super.setException(e);
        } finally {
            CommandStrategy.quorumCounter.incrementAndGet();
        }
    }
}
