package pt.ist.phonebook.replication;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import pt.ist.phonebook.shared.stubs.client.ContactDetailedReqType;

public class QuorumContactDetailedResponse extends QuorumResponse implements AsyncHandler<ContactDetailedReqType> {

    public QuorumContactDetailedResponse(String replicaURL) {
        super(replicaURL);
    }

    @Override
    public void handleResponse(Response<ContactDetailedReqType> res) {

        try {

            ContactDetailedReqType replicaResponse = res.get();
            System.out.println(">>>> [jUDDI] REPLICA VERSION AFTER: " + replicaResponse.getVersion());
            FrontEnd.addContactDetailedResponses(super.getReplicaURL(), replicaResponse);

        } catch (InterruptedException e) {
            System.out.println("Throwing original exception type is " + e.getCause().getClass().getName());
            super.setException(e);
        } catch (ExecutionException e) {
            super.setException(e);
        } finally {
            FrontEnd.quorumCounter++;
        }

    }

}
