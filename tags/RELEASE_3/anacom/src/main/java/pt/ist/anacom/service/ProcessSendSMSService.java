package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.domain.SMS;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessSendSMSService extends AnacomService {

	private SMSDto SMSDto;

	public ProcessSendSMSService(SMSDto SMSDto) {
		this.SMSDto = SMSDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		SMS sms = new SMS(SMSDto.getMessage());
		Phone senderPhone = anacom.getPhone(SMSDto.getSourceNumber());

		boolean sameOperator = false;
		if (SMSDto.getDestinationNumber().substring(0, Anacom.prefixLength) == senderPhone.getPrefix())
			sameOperator = true;

		int smsCost = senderPhone.getOperator().getPlan().calcCostSMS(sms.getMessage(), sameOperator);
		if (smsCost > senderPhone.getBalance())
			throw new InsuficientBalanceException(senderPhone.getNr(), senderPhone.getBalance());
		
		senderPhone.addSentSMS(sms);
		senderPhone.decreaseBalanceBy(smsCost);
	}
}
