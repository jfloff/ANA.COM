package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.GetCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumBalanceResponse;
import pt.ist.anacom.replication.vote.VoteList;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.BalanceType;
import pt.ist.anacom.shared.stubs.client.NoSuchPhoneRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorPrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.client.PhoneSimpleType;

public class GetPhoneBalanceStrategy extends GetCommandStrategy {

    private final PhoneSimpleDto phoneDto;
    private final PhoneSimpleType phoneSimpleTypeRequest;
    private final ConcurrentHashMap<String, BalanceType> responseList;

    public GetPhoneBalanceStrategy(BusinessQueryManager businessQueryManager, PhoneSimpleDto phoneDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.phoneDto = phoneDto;
        this.handlerList = new ArrayList<QuorumBalanceResponse>();

        phoneSimpleTypeRequest = new PhoneSimpleType();

        phoneSimpleTypeRequest.setPhoneNumber(phoneDto.getPhoneNumber());
        phoneSimpleTypeRequest.setVersion(nextVersion);

        responseList = new ConcurrentHashMap<String, BalanceType>();
    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async getPhoneBalanceAsync : " + phoneDto.getPhoneNumber() + " | New version : "
                    + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);
            }

            // Async Replica request
            if (port != null) {
                QuorumBalanceResponse handlerResponse = new QuorumBalanceResponse(url, this);
                addHandler(handlerResponse);
                System.out.println("NOVO HANDLER GET: " + handlerResponse);
                port.getPhoneBalanceAsync(phoneSimpleTypeRequest, handlerResponse);
            }
        }
    }

    @Override
    public void addResponse(String url, Object responseType) {
        responseList.put(url, (BalanceType) responseType);
    }

    @Override
    public void vote() {

        ArrayList<Integer> responseListVersion = new ArrayList<Integer>();
        ArrayList<Object> responseListObject = new ArrayList<Object>();

        for (BalanceType balanceType : responseList.values()) {
            responseListVersion.add(balanceType.getVersion());
            responseListObject.add(balanceType);
        }

        voteList = new VoteList(responseListVersion, responseListObject, responseList.keySet());
        System.out.println("[QUORUM] voteList -> " + voteList);

    }

    @Override
    public void getExtraResponse() {

        try {

            System.out.println("[EXTRA-QUORUM] Getting last server info");
            String lastServerURL = getReplicasNotResponded(responseList.keySet(), urlList);

            System.out.println("[EXTRA-QUORUM] Last Server : " + lastServerURL);
            AnacomPortType port = getPortType(lastServerURL);

            // Sync Replica request
            responseType = port.getPhoneBalance(phoneSimpleTypeRequest);

            // Save vote
            BalanceType type = (BalanceType) responseType;
            voteList.addVote(type.getVersion(), responseType);

            // Vote again
            responseType = voteList.getBestResponse(getReplicaLength());

            // If cant find best answer with all replicas then is catastrophe
            if (responseType == null) {
                System.out.println("[ERR] JA FOSTE");
                System.exit(-1);
            }

        } catch (NoSuchPhoneRemoteException e) {
            throw new NoSuchPhoneException(e.getFaultInfo().getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            throw new OperatorPrefixDoesNotExistException(e.getFaultInfo().getOperatorPrefix());
        }

    }

    public BalanceDto getBalance() {
        BalanceType type = (BalanceType) responseType;
        return  new BalanceDto(type.getBalance());
    }

}
