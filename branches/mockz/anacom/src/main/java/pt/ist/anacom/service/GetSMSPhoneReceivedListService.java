package pt.ist.anacom.service;

import java.util.List;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.SMS;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class GetSMSPhoneReceivedListService extends AnacomService {

    private PhoneSimpleDto phoneDto;
    private SMSPhoneReceivedListDto result;

    public GetSMSPhoneReceivedListService(PhoneSimpleDto phoneDto) {
        this.phoneDto = phoneDto;
    }

    @Override
    public final void dispatch() throws OperatorPrefixDoesNotExistException, NoSuchPhoneException {

        Anacom anacom = FenixFramework.getRoot();

        List<SMS> smsList = anacom.getSMSPhoneReceivedList(phoneDto.getPhoneNumber());
        this.result = new SMSPhoneReceivedListDto();

        for (SMS sms : smsList)
            result.add(sms.getSourcePhoneNumber(), sms.getDestinationPhoneNumber(), sms.getMessage());
    }

    public SMSPhoneReceivedListDto getSMSPhoneReceivedListServiceResult() {
        return this.result;
    }
}
