package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.VoiceEndCallDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessEndDestinationVoiceCallService extends AnacomService {

    private final VoiceEndCallDto voiceEndCallDto;

    public ProcessEndDestinationVoiceCallService(VoiceEndCallDto voiceEndCallDto) {
        this.voiceEndCallDto = voiceEndCallDto;
    }

    public VoiceEndCallDto getVoiceEndCallDto() {
        return voiceEndCallDto;
    }

    @Override
    public void dispatch() throws AnacomException {
        Anacom anacom = FenixFramework.getRoot();
        anacom.endDestinationVoiceCall(voiceEndCallDto.getSourcePhoneNumber(),
                                       voiceEndCallDto.getDestinationPhoneNumber(),
                                       voiceEndCallDto.getDuration());
    }

}
