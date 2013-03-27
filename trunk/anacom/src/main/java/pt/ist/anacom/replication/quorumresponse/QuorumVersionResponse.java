package pt.ist.anacom.replication.quorumresponse;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.CommandStrategy;

public class QuorumVersionResponse extends QuorumResponse implements AsyncHandler<Integer> {

    public QuorumVersionResponse(String replicaURL, CommandStrategy command) {
        super(replicaURL, command);
    }

    @Override
    public void handleResponse(Response<Integer> res) {

        try {

            // TODO devolver esta versao e fazer qq coisa com ela
            Integer replicaResponse = res.get();
            Logger.getLogger(this.getClass()).info(">>>> [LOG]REPLICA VERSION AFTER: " + replicaResponse);

        } catch (InterruptedException e) {
            Logger.getLogger(this.getClass()).info("[QUORUM] Throwing original exception type is " + e.getCause().getClass().getName());
            super.setException(e);
        } catch (ExecutionException e) {
            super.setException(e);
        } finally {
            CommandStrategy.quorumCounter.incrementAndGet();
        }

    }

}
