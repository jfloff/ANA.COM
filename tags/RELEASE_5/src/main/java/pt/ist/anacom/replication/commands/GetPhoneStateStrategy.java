package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.GetCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumBalanceResponse;
import pt.ist.anacom.replication.quorumresponse.QuorumStateResponse;
import pt.ist.anacom.replication.vote.StateTypeVoteList;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.NoSuchPhoneRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorPrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.client.PhoneSimpleType;
import pt.ist.anacom.shared.stubs.client.StateType;

public class GetPhoneStateStrategy extends GetCommandStrategy {

    private final PhoneSimpleDto phoneDto;
    private final PhoneSimpleType phoneSimpleTypeRequest;
    private final ConcurrentHashMap<String, StateType> responseList;

    public GetPhoneStateStrategy(BusinessQueryManager businessQueryManager, PhoneSimpleDto phoneDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.phoneDto = phoneDto;
        this.handlerList = new ArrayList<QuorumBalanceResponse>();

        phoneSimpleTypeRequest = new PhoneSimpleType();

        phoneSimpleTypeRequest.setPhoneNumber(phoneDto.getPhoneNumber());
        phoneSimpleTypeRequest.setVersion(nextVersion);

        responseList = new ConcurrentHashMap<String, StateType>();
    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async getPhoneStateAsync : " + phoneDto.getPhoneNumber() + " | New version : " + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);
            }

            // Async Replica request
            if (port != null) {
                QuorumStateResponse handlerResponse = new QuorumStateResponse(url, this);
                addHandler(handlerResponse);
                setSecurityHandlers(port);
                Logger.getLogger(this.getClass()).info("NOVO HANDLER GET: " + handlerResponse);
                port.getPhoneStateAsync(phoneSimpleTypeRequest, handlerResponse);
            }
        }
    }

    @Override
    public void addResponse(String url, Object responseType) {
        responseList.put(url, (StateType) responseType);
    }

    @Override
    public void vote() {

        ArrayList<Integer> responseListVersion = new ArrayList<Integer>();
        ArrayList<Object> responseListObject = new ArrayList<Object>();

        for (StateType stateType : responseList.values()) {
            responseListVersion.add(stateType.getVersion());
            responseListObject.add(stateType);
        }

        voteList = new StateTypeVoteList(responseListVersion, responseListObject, responseList.keySet());
        Logger.getLogger(this.getClass()).info("[QUORUM] voteList -> " + voteList);

    }

    @Override
    public void getExtraResponse() {
        try {
            Logger.getLogger(this.getClass()).info("[EXTRA-QUORUM] Getting last server info");
            String lastServerURL = getReplicasNotResponded(responseList.keySet(), urlList);

            Logger.getLogger(this.getClass()).info("[EXTRA-QUORUM] Last Server : " + lastServerURL);
            AnacomPortType port = getPortType(lastServerURL);

            // Sync Replica request
            responseType = port.getPhoneState(phoneSimpleTypeRequest);

            // Save vote
            StateType type = (StateType) responseType;
            voteList.addVote(type.getVersion(), responseType);

            // Vote again
            responseType = voteList.getBestResponse(getReplicaLength());

            // If cant find best answer with all replicas then is catastrophe
            if (responseType == null) {
                Logger.getLogger(this.getClass()).info("[ERR] JA FOSTE");
                System.exit(-1);
            }
        } catch (NoSuchPhoneRemoteException e) {
            throw new NoSuchPhoneException(e.getFaultInfo().getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            throw new OperatorPrefixDoesNotExistException(e.getFaultInfo().getOperatorPrefix());
        }
    }

    public StateDto getState() {
        StateType stateType = (StateType) responseType;
        Logger.getLogger(this.getClass()).info("ESTADOOOOOOOOOO: " + stateType.getPhoneState());
        return new StateDto(AnacomData.convertIntToStateEnum(stateType.getPhoneState()));
    }

}
