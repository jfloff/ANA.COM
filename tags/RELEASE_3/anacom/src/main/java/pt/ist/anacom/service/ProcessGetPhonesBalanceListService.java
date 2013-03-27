package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.shared.dto.PhonesAndBalanceListDto;
import pt.ist.anacom.shared.exception.OperatorDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessGetPhonesBalanceListService extends AnacomService {

	private PhonesAndBalanceListDto getPhonesBalanceDto;

	public ProcessGetPhonesBalanceListService(PhonesAndBalanceListDto getPhoneBalanceDto) {
		this.getPhonesBalanceDto = getPhoneBalanceDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		Operator operator = anacom.getOperatorByPrefix(getPhonesBalanceDto.getOperatorPrefix());
		if (operator == null)
			throw new OperatorDoesNotExistException(getPhonesBalanceDto.getOperatorPrefix());
		for (Phone phone : operator.getPhone())
			getPhonesBalanceDto.add(phone.getNr(), phone.getBalance());
	}
}
