package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneStateException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessStartDestinationVoiceCallService extends AnacomService {

    private CommunicationDto voiceCallDto;

    public ProcessStartDestinationVoiceCallService(CommunicationDto callDto) {
        this.voiceCallDto = callDto;
    }

    @Override
    public void dispatch() throws OperatorPrefixDoesNotExistException, NoSuchPhoneException, PhoneStateException {
        Anacom anacom = FenixFramework.getRoot();
        anacom.startDestinationVoiceCall(voiceCallDto.getSourcePhoneNumber(), voiceCallDto.getDestinationPhoneNumber());
    }
}
