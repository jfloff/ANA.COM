package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.NewGenPhone;
import pt.ist.anacom.shared.dto.PhoneDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessRegisterNewPhoneService extends AnacomService {

	private PhoneDto phoneDto;

	public ProcessRegisterNewPhoneService(PhoneDto phoneDto) {
		this.phoneDto = phoneDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		anacom.addPhone(this.phoneDto.getOperatorPrefix(), new NewGenPhone(this.phoneDto.getPhoneNumber()));
	}
}
