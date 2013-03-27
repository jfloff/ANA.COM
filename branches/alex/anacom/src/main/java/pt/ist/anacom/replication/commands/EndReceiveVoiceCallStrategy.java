package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.WriteCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumVersionResponse;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.CommunicationDurationType;

public class EndReceiveVoiceCallStrategy extends WriteCommandStrategy {

    private final CommunicationDurationDto communicationDurationDto;
    private final CommunicationDurationType communicationType;

    public EndReceiveVoiceCallStrategy(BusinessQueryManager businessQueryManager, CommunicationDurationDto communicationDurationDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this.communicationDurationDto = communicationDurationDto;

        communicationType = new CommunicationDurationType();
        communicationType.setSourcePhoneNumber(communicationDurationDto.getSourcePhoneNumber());
        communicationType.setDestinationPhoneNumber(communicationDurationDto.getDestinationPhoneNumber());
        communicationType.setDuration(communicationDurationDto.getDuration());
        communicationType.setVersion(nextVersion);

    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async endReceiveVoiceCall(" + communicationDurationDto.getDestinationPhoneNumber()
                    + ", " + communicationDurationDto.getDuration() + ") New version : " + nextVersion);

            try {
                port = getPortType(url);
            } catch (Exception e) {
                throw new CouldNotConnectException(prefix);

            }

            // Async Replica request
            if (port != null) {
                QuorumVersionResponse handlerResponse = new QuorumVersionResponse(url, this);
                addHandler(handlerResponse);
                port.endReceiveVoiceCallAsync(communicationType, handlerResponse);
            }
        }
    }

}
