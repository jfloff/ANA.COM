package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.WriteCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumVersionResponse;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.PhoneDetailedType;

public class RegisterPhoneStrategy extends WriteCommandStrategy {

    private final PhoneDetailedDto phoneDto;
    private final PhoneDetailedType phoneType;

    public RegisterPhoneStrategy(BusinessQueryManager businessQueryManager, PhoneDetailedDto phoneDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.phoneDto = phoneDto;

        phoneType = new PhoneDetailedType();

        phoneType.setPhoneNumber(phoneDto.getPhoneNumber());
        phoneType.setOperatorPrefix(phoneDto.getOperatorPrefix());
        phoneType.setPhoneGen(phoneDto.getPhoneGen().ordinal());
        phoneType.setVersion(nextVersion);

    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async registerPhone : " + phoneDto.getPhoneNumber() + " | New version : " + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);

            }

            // Async Replica request
            if (port != null) {
                QuorumVersionResponse handlerResponse = new QuorumVersionResponse(url, this);
                addHandler(handlerResponse);
                port.registerPhoneAsync(phoneType, handlerResponse);
            }
        }
    }

}
