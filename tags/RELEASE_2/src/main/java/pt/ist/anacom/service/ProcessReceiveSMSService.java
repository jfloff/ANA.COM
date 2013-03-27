package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Phone;
import pt.ist.anacom.domain.SMS;
import pt.ist.anacom.exception.NoSuchPhoneException;
import pt.ist.anacom.service.dto.SMSDto;
import pt.ist.fenixframework.FenixFramework;

public class ProcessReceiveSMSService extends AnacomService {

	private SMSDto SMSDto;

	public ProcessReceiveSMSService(SMSDto SMSDto) {
		this.SMSDto = SMSDto;
	}

	public final void dispatch() {
		Anacom anacom = FenixFramework.getRoot();
		SMS sms = new SMS(SMSDto.getText());
		Phone receiverPhone = anacom.getPhone(SMSDto.getReceiverNumber());
		if (receiverPhone == null)
			throw new NoSuchPhoneException(SMSDto.getReceiverNumber());
		receiverPhone.addReceivedSMS(sms);
	}

}
