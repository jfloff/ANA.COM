package pt.ist.anacom.service;


import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.fenixframework.FenixFramework;

public class CancelRegisteredPhoneService extends AnacomService {

    private PhoneSimpleDto phoneDto;


    public CancelRegisteredPhoneService(PhoneSimpleDto phoneDto) {
        this.phoneDto = phoneDto;
    }

    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();
        anacom.removePhone(phoneDto.getPhoneNumber());
    }
}
