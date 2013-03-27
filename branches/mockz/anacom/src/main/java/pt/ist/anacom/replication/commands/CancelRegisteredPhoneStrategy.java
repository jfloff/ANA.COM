package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.WriteCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumVersionResponse;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.PhoneSimpleType;

public class CancelRegisteredPhoneStrategy extends WriteCommandStrategy {

    private final PhoneSimpleDto phoneSimpleDto;
    private final PhoneSimpleType phoneSimpleType;

    public CancelRegisteredPhoneStrategy(BusinessQueryManager businessQueryManager, PhoneSimpleDto phoneSimpleDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.phoneSimpleDto = phoneSimpleDto;

        phoneSimpleType = new PhoneSimpleType();
        phoneSimpleType.setPhoneNumber(phoneSimpleDto.getPhoneNumber());
        phoneSimpleType.setVersion(nextVersion);

    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async cancelRegisteredPhone : " + phoneSimpleDto.getPhoneNumber() + " | New version : "
                    + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);

            }

            // Async Replica request
            if (port != null) {
                QuorumVersionResponse handlerResponse = new QuorumVersionResponse(url, this);
                addHandler(handlerResponse);
                port.cancelRegisteredPhoneAsync(phoneSimpleType, handlerResponse);
            }
        }
    }

}
