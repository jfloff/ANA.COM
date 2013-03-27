package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Communication;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.fenixframework.FenixFramework;

public class GetLastMadeCommunicationService extends AnacomService {

    private PhoneSimpleDto phoneDto;
    private CommunicationDto result;

    public GetLastMadeCommunicationService(PhoneSimpleDto phoneDto) {
        this.phoneDto = phoneDto;
    }

    public final void dispatch() {
        Anacom anacom = FenixFramework.getRoot();

        Communication communication = anacom.getPhone(phoneDto.getPhoneNumber()).getLastCommunicationMade();

        if (communication != null) {
            AnacomData.CommunicationType type = anacom.getCommunicationType(communication);
            int length = anacom.getCommunicationLength(communication, type);

            this.result = new CommunicationDto(type, communication.getDestinationPhoneNumber(), communication.getCost(), length);
        } else
            this.result = null;
    }

    public CommunicationDto getLastMadeCommunicationServiceResult() {
        return this.result;
    }
}
