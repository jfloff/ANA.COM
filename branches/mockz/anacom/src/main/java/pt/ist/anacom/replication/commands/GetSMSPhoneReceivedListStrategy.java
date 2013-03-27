package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.GetCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumBalanceResponse;
import pt.ist.anacom.replication.quorumresponse.QuorumSMSPhoneReceivedListResponse;
import pt.ist.anacom.replication.vote.VoteList;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.NoSuchPhoneRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorPrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.client.PhoneSimpleType;
import pt.ist.anacom.shared.stubs.client.SMSPhoneReceivedListType;
import pt.ist.anacom.shared.stubs.client.SMSType;

public class GetSMSPhoneReceivedListStrategy extends GetCommandStrategy {

    private final PhoneSimpleDto phoneDto;
    private final PhoneSimpleType phoneSimpleTypeRequest;
    private final ConcurrentHashMap<String, SMSPhoneReceivedListType> responseList;

    public GetSMSPhoneReceivedListStrategy(BusinessQueryManager businessQueryManager, PhoneSimpleDto phoneDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.phoneDto = phoneDto;
        this.handlerList = new ArrayList<QuorumBalanceResponse>();

        phoneSimpleTypeRequest = new PhoneSimpleType();

        phoneSimpleTypeRequest.setPhoneNumber(phoneDto.getPhoneNumber());
        phoneSimpleTypeRequest.setVersion(nextVersion);

        responseList = new ConcurrentHashMap<String, SMSPhoneReceivedListType>();
    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async getSMSPhoneReceivedList : " + phoneDto.getPhoneNumber() + " | New version : " + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                System.out.println("[ERR] Nao foi possivel contactar a replica: " + url);
            }

            // Async Replica request
            if (port != null) {
                QuorumSMSPhoneReceivedListResponse handlerResponse = new QuorumSMSPhoneReceivedListResponse(url, this);
                addHandler(handlerResponse);
                System.out.println("NOVO HANDLER GET: " + handlerResponse);
                port.getSMSPhoneReceivedListAsync(phoneSimpleTypeRequest, handlerResponse);
            }
        }
    }

    @Override
    public void addResponse(String url, Object responseType) {
        responseList.put(url, (SMSPhoneReceivedListType) responseType);
    }

    @Override
    public void vote() {

        ArrayList<Integer> responseListVersion = new ArrayList<Integer>();
        ArrayList<Object> responseListObject = new ArrayList<Object>();

        for (SMSPhoneReceivedListType smsPhoneReceivedListType : responseList.values()) {
            responseListVersion.add(smsPhoneReceivedListType.getVersion());
            responseListObject.add(smsPhoneReceivedListType);
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
            responseType = port.getSMSPhoneReceivedList(phoneSimpleTypeRequest);

            // Save vote
            SMSPhoneReceivedListType type = (SMSPhoneReceivedListType) responseType;
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


    public SMSPhoneReceivedListDto getSMSPhoneReceivedList() {

        SMSPhoneReceivedListDto dto = new SMSPhoneReceivedListDto();
        SMSPhoneReceivedListType smsPhoneReceivedListType = (SMSPhoneReceivedListType) responseType;
        
        for(SMSType sms: smsPhoneReceivedListType.getSmsList())
            dto.add(sms.getSourcePhoneNumber(), sms.getDestinationPhoneNumber(), sms.getMessage());
        

        return dto;
    }

}
