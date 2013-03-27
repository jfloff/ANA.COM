package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.CommunicationOut;
import pt.ist.anacom.shared.dto.CommunicationOutDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.fenixframework.FenixFramework;

public class GetLastMadeCommunicationService extends AnacomService {

    private final PhoneSimpleDto phoneDto;
    private CommunicationOutDto result;

    public GetLastMadeCommunicationService(PhoneSimpleDto phoneDto) {
        this.phoneDto = phoneDto;
    }

    @Override
    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();

        CommunicationOut communication = (CommunicationOut) anacom.getPhone(phoneDto.getPhoneNumber()).getLastCommunicationMade();

        result = new CommunicationOutDto(communication.getType(), communication.getDestinationPhoneNumber(), communication.getCost(),
                communication.getLength());
    }

    public CommunicationOutDto getLastMadeCommunicationServiceResult() {
        return this.result;
    }
}
