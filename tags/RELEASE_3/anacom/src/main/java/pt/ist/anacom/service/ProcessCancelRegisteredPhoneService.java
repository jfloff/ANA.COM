package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.PhoneDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessCancelRegisteredPhoneService extends AnacomService {

	private PhoneDto phoneDto;

	public ProcessCancelRegisteredPhoneService(PhoneDto phoneDto) {
		this.phoneDto = phoneDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		anacom.removePhone(this.phoneDto.getOperatorPrefix(), phoneDto.getPhoneNumber());
	}
}
