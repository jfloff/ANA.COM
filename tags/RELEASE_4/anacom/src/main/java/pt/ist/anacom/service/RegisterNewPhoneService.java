package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.fenixframework.FenixFramework;

public class RegisterNewPhoneService extends AnacomService {

    private PhoneDetailedDto phoneDto;

    public RegisterNewPhoneService(PhoneDetailedDto phoneDto) {
        this.phoneDto = phoneDto;
    }

    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();
        anacom.addPhone(this.phoneDto.getOperatorPrefix(), this.phoneDto.getPhoneNumber(), this.phoneDto.getPhoneGen());
    }
}
