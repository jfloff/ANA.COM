package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.exception.CouldNotSetStateWhileCommunicationActiveException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class SetPhoneStateService extends AnacomService {

    private PhoneAndStateDto phoneStatusDto;

    public SetPhoneStateService(PhoneAndStateDto phoneStatusDto) {
        this.phoneStatusDto = phoneStatusDto;
    }

    public final void dispatch() throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, CouldNotSetStateWhileCommunicationActiveException {
        Anacom anacom = FenixFramework.getRoot();
        anacom.setPhoneState(phoneStatusDto.getPhoneNumber(), phoneStatusDto.getState());
    }
}
