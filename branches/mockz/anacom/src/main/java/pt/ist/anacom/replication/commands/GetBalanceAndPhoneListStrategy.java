package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.GetCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumBalanceAndPhoneListResponse;
import pt.ist.anacom.replication.vote.VoteList;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.BalanceAndPhoneListType;
import pt.ist.anacom.shared.stubs.client.BalanceAndPhoneType;
import pt.ist.anacom.shared.stubs.client.OperatorPrefixDoesNotExistRemoteException;
import pt.ist.anacom.shared.stubs.client.OperatorSimpleType;

public class GetBalanceAndPhoneListStrategy extends GetCommandStrategy {

    private final OperatorSimpleDto operatorDto;
    private final OperatorSimpleType operatorSimpleTypeRequest;
    private final ConcurrentHashMap<String, BalanceAndPhoneListType> responseList;

    public GetBalanceAndPhoneListStrategy(BusinessQueryManager businessQueryManager, OperatorSimpleDto operatorDto) throws MalformedURLException {

        super(operatorDto.getOperatorPrefix(), businessQueryManager);
        this.operatorDto = operatorDto;
        this.handlerList = new ArrayList<QuorumBalanceAndPhoneListResponse>();

        operatorSimpleTypeRequest = new OperatorSimpleType();

        operatorSimpleTypeRequest.setOperatorPrefix(operatorDto.getOperatorPrefix());
        operatorSimpleTypeRequest.setVersion(nextVersion);

        responseList = new ConcurrentHashMap<String, BalanceAndPhoneListType>();
    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async getBalanceAndPhoneList : " + operatorDto.getOperatorPrefix() + " | New version : " + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                System.out.println("[ERR] Nao foi possivel contactar a replica: " + url);
            }

            // Async Replica request
            if (port != null) {
                QuorumBalanceAndPhoneListResponse handlerResponse = new QuorumBalanceAndPhoneListResponse(url, this);
                addHandler(handlerResponse);
                System.out.println("NOVO HANDLER GET: " + handlerResponse);
                port.getBalanceAndPhoneListAsync(operatorSimpleTypeRequest, handlerResponse);
            }
        }
    }

    @Override
    public void addResponse(String url, Object responseType) {
        responseList.put(url, (BalanceAndPhoneListType) responseType);
    }

    @Override
    public void vote() {

        ArrayList<Integer> responseListVersion = new ArrayList<Integer>();
        ArrayList<Object> responseListObject = new ArrayList<Object>();

        for (BalanceAndPhoneListType balanceAndPhoneListType : responseList.values()) {
            responseListVersion.add(balanceAndPhoneListType.getVersion());
            responseListObject.add(balanceAndPhoneListType);
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
            responseType = port.getBalanceAndPhoneList(operatorSimpleTypeRequest);

            // Save vote
            BalanceAndPhoneListType type = (BalanceAndPhoneListType) responseType;
            voteList.addVote(type.getVersion(), responseType);

            // Vote again
            responseType = voteList.getBestResponse(getReplicaLength());

            // If cant find best answer with all replicas then is catastrophe
            if (responseType == null) {
                System.out.println("[ERR] JA FOSTE");
                System.exit(-1);
            }
        } catch (OperatorPrefixDoesNotExistRemoteException e) {
            throw new OperatorPrefixDoesNotExistException(e.getFaultInfo().getOperatorPrefix());
        }
    }


    public BalanceAndPhoneListDto getBalanceAndPhoneList() {

        BalanceAndPhoneListDto dto = new BalanceAndPhoneListDto();
        BalanceAndPhoneListType balanceAndPhoneListType = (BalanceAndPhoneListType) responseType;
        
        for(BalanceAndPhoneType phone: balanceAndPhoneListType.getPhoneList())
            dto.add(phone.getPhoneNumber(), phone.getBalance());
        

        return dto;
    }

}
