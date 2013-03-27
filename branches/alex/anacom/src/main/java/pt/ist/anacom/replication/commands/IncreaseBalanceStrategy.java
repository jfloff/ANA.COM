package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.WriteCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumVersionResponse;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.BalanceAndPhoneType;

public class IncreaseBalanceStrategy extends WriteCommandStrategy {

    private final BalanceAndPhoneDto incBalDto;
    private final BalanceAndPhoneType balanceAndPhoneType;

    public IncreaseBalanceStrategy(BusinessQueryManager businessQueryManager, BalanceAndPhoneDto incBalDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.incBalDto = incBalDto;

        balanceAndPhoneType = new BalanceAndPhoneType();

        balanceAndPhoneType.setPhoneNumber(incBalDto.getPhoneNumber());
        balanceAndPhoneType.setBalance(incBalDto.getBalance());
        balanceAndPhoneType.setVersion(nextVersion);

    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async IncreaseBalance : " + incBalDto.getBalance() + " | New version : " + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);

            }

            // Async Replica request
            if (port != null) {
                QuorumVersionResponse handlerResponse = new QuorumVersionResponse(url, this);
                addHandler(handlerResponse);
                port.increasePhoneBalanceAsync(balanceAndPhoneType, handlerResponse);
            }
        }
    }
}
