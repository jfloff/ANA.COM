package pt.ist.anacom.service;


import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class CancelRegisteredPhoneService extends AnacomService {

    private PhoneSimpleDto phoneDto;


    public CancelRegisteredPhoneService(PhoneSimpleDto phoneDto) {
        this.phoneDto = phoneDto;
    }

    @Override
    public final void dispatch() throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        Anacom anacom = FenixFramework.getRoot();
        anacom.removePhone(phoneDto.getPhoneNumber());
    }
}
