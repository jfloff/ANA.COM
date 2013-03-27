package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.WriteCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumVersionResponse;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.PhoneAndStateType;

public class SetPhoneStateStrategy extends WriteCommandStrategy {

    private final PhoneAndStateDto phoneAndStateDto;
    private final PhoneAndStateType phoneAndStateType;

    public SetPhoneStateStrategy(BusinessQueryManager businessQueryManager, PhoneAndStateDto phoneAndStateDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.phoneAndStateDto = phoneAndStateDto;

        phoneAndStateType = new PhoneAndStateType();

        phoneAndStateType.setPhoneNumber(phoneAndStateDto.getPhoneNumber());
        phoneAndStateType.setPhoneState(phoneAndStateDto.getState().ordinal());
        phoneAndStateType.setVersion(nextVersion);

    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async setPhoneState(" + phoneAndStateDto.getPhoneNumber() + ", "
                    + phoneAndStateDto.getState().ordinal() + ") New version : " + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);

            }

            // Async Replica request
            if (port != null) {
                QuorumVersionResponse handlerResponse = new QuorumVersionResponse(url, this);
                addHandler(handlerResponse);
                port.setPhoneStateAsync(phoneAndStateType, handlerResponse);
            }
        }
    }
}
