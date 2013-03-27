package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.WriteCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumVersionResponse;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.CommunicationType;

public class StartReceiveVoiceCallStrategy extends WriteCommandStrategy {

    private final CommunicationDto communicationDto;
    private final CommunicationType communicationType;

    public StartReceiveVoiceCallStrategy(BusinessQueryManager businessQueryManager, CommunicationDto communicationDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.communicationDto = communicationDto;

        communicationType = new CommunicationType();
        communicationType.setSourcePhoneNumber(communicationDto.getSourcePhoneNumber());
        communicationType.setDestinationPhoneNumber(communicationDto.getDestinationPhoneNumber());
        communicationType.setVersion(nextVersion);

    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async startReceiveVoiceCall(" + communicationDto.getDestinationPhoneNumber()
                    + ") New version : " + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);

            }

            // Async Replica request
            if (port != null) {
                QuorumVersionResponse handlerResponse = new QuorumVersionResponse(url, this);
                addHandler(handlerResponse);
                setSecurityHandlers(port);
                port.startReceiveVoiceCallAsync(communicationType, handlerResponse);
            }
        }
    }
}
