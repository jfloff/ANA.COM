package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class GetPhoneStateService extends AnacomService {

    private PhoneSimpleDto phoneDto;
    private StateDto result;

    public GetPhoneStateService(PhoneSimpleDto phoneDto) {
        this.phoneDto = phoneDto;
    }

    public final void dispatch() throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        Anacom anacom = FenixFramework.getRoot();
        AnacomData.State state = anacom.getPhoneState(this.phoneDto.getPhoneNumber());
        result = new StateDto(state);
    }

    public StateDto getPhoneStateServiceResult() {
        return this.result;
    }

}
