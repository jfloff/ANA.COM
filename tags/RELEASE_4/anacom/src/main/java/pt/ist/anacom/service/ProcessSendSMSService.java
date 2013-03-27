package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessSendSMSService extends AnacomService {

    private SMSDto SMSDto;

    public ProcessSendSMSService(SMSDto SMSDto) {
        this.SMSDto = SMSDto;
    }

    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();
        anacom.sendSMS(SMSDto.getSourceNumber(), SMSDto.getDestinationNumber(), SMSDto.getMessage());
    }
}
