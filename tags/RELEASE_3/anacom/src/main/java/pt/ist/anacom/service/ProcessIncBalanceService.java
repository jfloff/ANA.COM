package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessIncBalanceService extends AnacomService {

	private BalanceDto incBalDto;

	public ProcessIncBalanceService(BalanceDto incBalDto) {
		this.incBalDto = incBalDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		Phone phone = anacom.getPhone(incBalDto.getNumber());
		phone.increaseBalanceBy(incBalDto.getBalance());
	}
}
