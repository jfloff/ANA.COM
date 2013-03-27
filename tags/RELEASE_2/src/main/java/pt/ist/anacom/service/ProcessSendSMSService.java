package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.domain.SMS;
import pt.ist.anacom.exception.InsuficientBalanceException;
import pt.ist.anacom.exception.NoSuchPhoneException;
import pt.ist.anacom.service.dto.SMSDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessSendSMSService extends AnacomService {

	private SMSDto SMSDto;

	public ProcessSendSMSService(SMSDto SMSDto) {
		this.SMSDto = SMSDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		SMS sms = new SMS(SMSDto.getText());
		Phone senderPhone = anacom.getPhone(SMSDto.getSenderNumber());
		
		if (senderPhone == null)
			throw new NoSuchPhoneException(SMSDto.getSenderNumber());

		boolean sameOperator = false;
		if (SMSDto.getReceiverNumber().substring(0, Anacom.prefixLength) == senderPhone.getPrefix())
			sameOperator = true;

		int smsCost = senderPhone.getOperator().getPlan().calcCostSMS(sms.getMessage(), sameOperator);
		if (smsCost > senderPhone.getBalance())
			throw new InsuficientBalanceException(senderPhone);
		senderPhone.addSentSMS(sms);
		senderPhone.decreaseBalanceBy(smsCost);
	}
}
