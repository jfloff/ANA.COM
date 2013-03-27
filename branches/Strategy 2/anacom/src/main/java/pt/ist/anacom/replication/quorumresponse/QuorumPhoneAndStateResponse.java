package pt.ist.anacom.replication.quorumresponse;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import pt.ist.anacom.replication.CommandStrategy;
import pt.ist.anacom.shared.stubs.client.PhoneAndStateType;

public class QuorumPhoneAndStateResponse extends QuorumResponse implements AsyncHandler<PhoneAndStateType> {

    public QuorumPhoneAndStateResponse(String replicaURL, CommandStrategy command) {
        super(replicaURL, command);
    }

    @Override
    public void handleResponse(Response<PhoneAndStateType> res) {

        try {
            PhoneAndStateType replicaResponse = res.get();

            System.out.println(">>>> [jUDDI] REPLICA VERSION AFTER: " + replicaResponse.getVersion());
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
