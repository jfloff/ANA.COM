package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.GetCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumBalanceResponse;
import pt.ist.anacom.replication.quorumresponse.QuorumLastCommunicationResponse;
import pt.ist.anacom.replication.vote.VoteList;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.LastCommunicationType;
import pt.ist.anacom.shared.stubs.client.NoCommunicationsMadeYetRemoteException;
import pt.ist.anacom.shared.stubs.client.NoSuchPhoneRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorPrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.client.PhoneSimpleType;

public class GetLastCommunicationStrategy extends GetCommandStrategy {

    private final PhoneSimpleDto phoneDto;
    private final PhoneSimpleType phoneSimpleTypeRequest;
    private final ConcurrentHashMap<String, LastCommunicationType> responseList;

    public GetLastCommunicationStrategy(BusinessQueryManager businessQueryManager, PhoneSimpleDto phoneDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.phoneDto = phoneDto;
        this.handlerList = new ArrayList<QuorumBalanceResponse>();
        phoneSimpleTypeRequest = new PhoneSimpleType();

        phoneSimpleTypeRequest.setPhoneNumber(phoneDto.getPhoneNumber());
        phoneSimpleTypeRequest.setVersion(nextVersion);

        responseList = new ConcurrentHashMap<String, LastCommunicationType>();
    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async getLastCommunicationAsync : " + phoneDto.getPhoneNumber() + " | New version : " + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);
            }

            // Async Replica request
            if (port != null) {
                QuorumLastCommunicationResponse handlerResponse = new QuorumLastCommunicationResponse(url, this);
                addHandler(handlerResponse);
                System.out.println("NOVO HANDLER GET: " + handlerResponse);
                port.getPhoneLastMadeCommunicationAsync(phoneSimpleTypeRequest, handlerResponse);
            }
        }
    }

    @Override
    public void addResponse(String url, Object responseType) {
        responseList.put(url, (LastCommunicationType) responseType);
    }

    @Override
    public void vote() {

        ArrayList<Integer> responseListVersion = new ArrayList<Integer>();
        ArrayList<Object> responseListObject = new ArrayList<Object>();

        for (LastCommunicationType lastCommunicationType : responseList.values()) {
            responseListVersion.add(lastCommunicationType.getVersion());
            responseListObject.add(lastCommunicationType);
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
            responseType = port.getPhoneLastMadeCommunication(phoneSimpleTypeRequest);

            // Save vote
            LastCommunicationType type = (LastCommunicationType) responseType;
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
        } catch (NoCommunicationsMadeYetRemoteException e) {
            throw new NoCommunicationsMadeYetException(e.getFaultInfo().getPhoneNumber());
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            throw new OperatorPrefixDoesNotExistException(e.getFaultInfo().getOperatorPrefix());
        }
    }


    public LastCommunicationDto getLastCommunication() {
        
        LastCommunicationType lastCommunicationType = (LastCommunicationType) responseType;

        LastCommunicationDto dto = new LastCommunicationDto(AnacomData.convertIntToCommunicationTypeEnum(lastCommunicationType.getType()),  
                    lastCommunicationType.getDestinationPhoneNumber(), lastCommunicationType.getCost(), lastCommunicationType.getLength());

        return dto;
    }

}
