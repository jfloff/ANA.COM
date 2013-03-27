package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Operator;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.domain.SMS;
import pt.ist.anacom.shared.dto.PhoneReceivedSMSListDto;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessGetPhoneReceivedSMSListService extends AnacomService {

	private PhoneReceivedSMSListDto receivedSMSListDto;

	public ProcessGetPhoneReceivedSMSListService(PhoneReceivedSMSListDto receivedSMSListDto) {
		this.receivedSMSListDto = receivedSMSListDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		Operator operator = anacom.getOperatorByPrefix(receivedSMSListDto.getOperatorPrefix());
		
		if (operator == null)
			throw new OperatorDoesNotExistException(receivedSMSListDto.getOperatorPrefix());
		
		Phone phone = operator.getPhoneByNr(receivedSMSListDto.getPhoneNumber());
		if(phone == null)
			throw new NoSuchPhoneException(receivedSMSListDto.getPhoneNumber());
		
		for (SMS sms : phone.getReceivedSMS()) {
			receivedSMSListDto.add(sms.getNrSource(), sms.getNrDest(), sms.getMessage());
		}
	}
}
