package pt.ist.anacom.replication.quorumresponse;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import pt.ist.anacom.replication.CommandStrategy;
import pt.ist.anacom.shared.stubs.client.PhoneSimpleType;

public class QuorumPhoneSimpleResponse extends QuorumResponse implements AsyncHandler<PhoneSimpleType> {

    public QuorumPhoneSimpleResponse(String replicaURL, CommandStrategy command) {
        super(replicaURL, command);
    }

    @Override
    public void handleResponse(Response<PhoneSimpleType> res) {

        try {
            PhoneSimpleType replicaResponse = res.get();

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
