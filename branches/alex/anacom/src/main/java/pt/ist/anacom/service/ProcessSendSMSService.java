package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneStateException;
import pt.ist.anacom.shared.exception.SMSInvalidTextException;
import pt.ist.fenixframework.FenixFramework;

public class ProcessSendSMSService extends AnacomService {

    private SMSDto SMSDto;

    public ProcessSendSMSService(SMSDto SMSDto) {
        this.SMSDto = SMSDto;
    }

    @Override
    public final void dispatch() throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            SMSInvalidTextException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            PhoneStateException {

        Anacom anacom = FenixFramework.getRoot();
        anacom.sendSMS(SMSDto.getSourcePhoneNumber(), SMSDto.getDestinationPhoneNumber(), SMSDto.getMessage());
    }

}
