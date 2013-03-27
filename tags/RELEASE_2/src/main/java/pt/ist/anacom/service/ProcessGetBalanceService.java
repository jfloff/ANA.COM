package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.exception.NoSuchPhoneException;
import pt.ist.anacom.service.dto.BalanceDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessGetBalanceService extends AnacomService {

	private BalanceDto getBalDto;

	public ProcessGetBalanceService(BalanceDto getBalDto) {
		this.getBalDto = getBalDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		Phone phone = anacom.getPhone(getBalDto.getNumber());
		if (phone == null)
			throw new NoSuchPhoneException(getBalDto.getNumber());
		getBalDto.balance = phone.getBalance();
	}
}
