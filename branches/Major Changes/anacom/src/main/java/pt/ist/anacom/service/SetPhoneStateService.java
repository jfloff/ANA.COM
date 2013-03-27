package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.StateAndPhoneDto;
import pt.ist.fenixframework.FenixFramework;

public class SetPhoneStateService extends AnacomService {

    private StateAndPhoneDto phoneStatusDto;

    public SetPhoneStateService(StateAndPhoneDto phoneStatusDto) {
        this.phoneStatusDto = phoneStatusDto;
    }

    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();
        anacom.setPhoneState(phoneStatusDto.getPhoneNumber(), phoneStatusDto.getState());
    }
}
