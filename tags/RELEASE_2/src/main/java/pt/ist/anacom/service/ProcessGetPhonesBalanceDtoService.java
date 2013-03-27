package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.exception.OperatorDoesNotExistException;
import pt.ist.anacom.service.dto.PhoneAndBalanceListDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessGetPhonesBalanceDtoService extends AnacomService {

	private PhoneAndBalanceListDto getPhoneBalanceDto;

	public ProcessGetPhonesBalanceDtoService(PhoneAndBalanceListDto getPhoneBalanceDto) {
		this.getPhoneBalanceDto = getPhoneBalanceDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		Operator operator = anacom.getOperatorByString(getPhoneBalanceDto.getOperatorName());
		if (operator == null)
			throw new OperatorDoesNotExistException(getPhoneBalanceDto.getOperatorName());
		for (Phone phone : operator.getPhone())
			getPhoneBalanceDto.add(phone.getNr(), phone.getBalance());
	}
}
