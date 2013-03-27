package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.VoiceEndCallDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessEndSourceVoiceCallService extends AnacomService {

    private final VoiceEndCallDto voiceEndCallDto;

    public ProcessEndSourceVoiceCallService(VoiceEndCallDto voiceEndCallDto) {
        this.voiceEndCallDto = voiceEndCallDto;
    }

    public VoiceEndCallDto getVoiceEndCallDto() {
        return voiceEndCallDto;
    }

    @Override
    public void dispatch() throws AnacomException {
        Anacom anacom = FenixFramework.getRoot();
        anacom.endSourceVoiceCall(voiceEndCallDto.getSourcePhoneNumber(), voiceEndCallDto.getDestinationPhoneNumber(), voiceEndCallDto.getDuration());
    }

}
