package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.exception.InvalidDurationException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessEndDestinationVoiceCallService extends AnacomService {

    private final CommunicationDurationDto voiceEndCallDto;

    public ProcessEndDestinationVoiceCallService(CommunicationDurationDto voiceEndCallDto) {
        this.voiceEndCallDto = voiceEndCallDto;
    }

    public CommunicationDurationDto getVoiceEndCallDto() {
        return voiceEndCallDto;
    }

    @Override
    public void dispatch() throws OperatorPrefixDoesNotExistException, NoSuchPhoneException, InvalidDurationException, NoActiveCommunicationException {
        Anacom anacom = FenixFramework.getRoot();
        anacom.endDestinationVoiceCall(voiceEndCallDto.getSourcePhoneNumber(),
                                       voiceEndCallDto.getDestinationPhoneNumber(),
                                       voiceEndCallDto.getDuration());
    }

}
