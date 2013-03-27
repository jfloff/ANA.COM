package pt.ist.anacom.replication.commands;

import java.net.MalformedURLException;

import javax.xml.registry.BusinessQueryManager;

import org.apache.log4j.Logger;

import pt.ist.anacom.replication.WriteCommandStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumVersionResponse;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.exception.CouldNotConnectException;
import pt.ist.anacom.shared.stubs.client.AnacomPortType;
import pt.ist.anacom.shared.stubs.client.SMSType;

public class ReceivedSMSStrategy extends WriteCommandStrategy {

    private final SMSDto _SMSDto;
    private final SMSType _SMSType;

    public ReceivedSMSStrategy(BusinessQueryManager businessQueryManager, SMSDto _SMSDto, String prefix) throws MalformedURLException {

        super(prefix, businessQueryManager);
        this._SMSDto = _SMSDto;

        _SMSType = new SMSType();
        _SMSType.setSourcePhoneNumber(_SMSDto.getSourcePhoneNumber());
        _SMSType.setDestinationPhoneNumber(_SMSDto.getDestinationPhoneNumber());
        _SMSType.setMessage(_SMSDto.getMessage());
        _SMSType.setVersion(nextVersion);

    }

    @Override
    public void sendToReplicas() {
        for (String url : urlList) {

            AnacomPortType port = null;

            Logger.getLogger(this.getClass()).info("[ANACOM] Async receiveSMS(" + _SMSDto.getDestinationPhoneNumber() + ") New version : "
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
                port.receiveSMSAsync(_SMSType, handlerResponse);
            }
        }
    }
}
