package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneStateException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessStartSourceVoiceCallService extends AnacomService {

    private CommunicationDto voiceCallDto;

    public ProcessStartSourceVoiceCallService(CommunicationDto callDto) {
        this.voiceCallDto = callDto;
    }

    @Override
    public void dispatch() throws OperatorPrefixDoesNotExistException, NoSuchPhoneException, InsuficientBalanceException, PhoneStateException {
        Anacom anacom = FenixFramework.getRoot();
        anacom.startSourceVoiceCall(voiceCallDto.getSourcePhoneNumber(), voiceCallDto.getDestinationPhoneNumber());
    }

}
