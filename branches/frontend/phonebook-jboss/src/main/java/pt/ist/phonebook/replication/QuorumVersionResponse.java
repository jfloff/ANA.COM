package pt.ist.phonebook.replication;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

public class QuorumVersionResponse extends QuorumResponse implements AsyncHandler<BigInteger> {

    public QuorumVersionResponse(String replicaURL) {
        super(replicaURL);
    }

    @Override
    public void handleResponse(Response<BigInteger> res) {

        try {

            // TODO devolver esta versao e fazer qq coisa com ela
            BigInteger replicaResponse = res.get();
            System.out.println(">>>> [jUDDI] REPLICA VERSION AFTER: " + replicaResponse);

        } catch (InterruptedException e) {
            System.out.println("[QUORUM] Throwing original exception type is " + e.getCause().getClass().getName());
            super.setException(e);
        } catch (ExecutionException e) {
            super.setException(e);
        } finally {
            FrontEnd.quorumCounter++;
        }

    }

}
