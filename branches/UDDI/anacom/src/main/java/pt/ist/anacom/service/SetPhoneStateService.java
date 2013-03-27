package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.fenixframework.FenixFramework;

public class SetPhoneStateService extends AnacomService {

    private PhoneAndStateDto phoneStatusDto;

    public SetPhoneStateService(PhoneAndStateDto phoneStatusDto) {
        this.phoneStatusDto = phoneStatusDto;
    }

    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();
        anacom.setPhoneState(phoneStatusDto.getPhoneNumber(), phoneStatusDto.getState());
    }
}
