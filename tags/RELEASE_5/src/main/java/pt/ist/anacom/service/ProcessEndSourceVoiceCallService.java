package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidDurationException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessEndSourceVoiceCallService extends AnacomService {

    private final CommunicationDurationDto voiceEndCallDto;

    public ProcessEndSourceVoiceCallService(CommunicationDurationDto voiceEndCallDto) {
        this.voiceEndCallDto = voiceEndCallDto;
    }

    public CommunicationDurationDto getVoiceEndCallDto() {
        return voiceEndCallDto;
    }

    @Override
    public void dispatch() throws OperatorPrefixDoesNotExistException,
            NoSuchPhoneException,
            InsuficientBalanceException,
            NegativeBalanceValueException,
            InvalidDurationException,
            NoActiveCommunicationException {
        Anacom anacom = FenixFramework.getRoot();
        anacom.endSourceVoiceCall(voiceEndCallDto.getSourcePhoneNumber(), voiceEndCallDto.getDestinationPhoneNumber(), voiceEndCallDto.getDuration());
    }
}
