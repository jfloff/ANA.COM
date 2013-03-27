package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.VoiceCallDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessStartDestinationVoiceCallService extends AnacomService {

	private VoiceCallDto voiceCallDto;

	public ProcessStartDestinationVoiceCallService(VoiceCallDto callDto){
		this.voiceCallDto = callDto;
	}
	
	@Override
	public void dispatch() throws AnacomException {
		Anacom anacom = FenixFramework.getRoot();
		anacom.startSourceVoiceCall(voiceCallDto.getDestinationPhoneNumber());
	}

}