package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.service.dto.PhoneDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessCancelRegisterPhoneService extends AnacomService {

	private PhoneDto phoneDto;

	public ProcessCancelRegisterPhoneService(PhoneDto phoneDto) {
		this.phoneDto = phoneDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		anacom.removePhone(this.phoneDto.getOperator(), phoneDto.getPhoneNumber());
	}
}
