package pt.ist.anacom.service;

import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.domain.Communication;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.fenixframework.FenixFramework;

public class GetLastMadeCommunicationService extends AnacomService {

    private PhoneSimpleDto phoneDto;
    private LastCommunicationDto result;

    public GetLastMadeCommunicationService(PhoneSimpleDto phoneDto) {
        this.phoneDto = phoneDto;
    }

    @Override
    public final void dispatch() throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, NoCommunicationsMadeYetException {

        Anacom anacom = FenixFramework.getRoot();

        Communication communication = anacom.getPhoneLastCommunicationMade(phoneDto.getPhoneNumber());

        AnacomData.CommunicationType type = anacom.getCommunicationType(communication);
        int length = anacom.getCommunicationLength(communication, type);

        this.result = new LastCommunicationDto(type, communication.getDestinationPhoneNumber(), communication.getCost(), length);
    }

    public LastCommunicationDto getLastMadeCommunicationServiceResult() {
        return this.result;
    }
}
