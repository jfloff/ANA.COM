package pt.ist.anacom.presentationserver.server;

import javax.xml.ws.BindingProvider;

import pt.ist.anacom.replication.FrontEnd;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidDurationException;
import pt.ist.anacom.shared.exception.InvalidTaxException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorNameAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorNullNameException;
import pt.ist.anacom.shared.exception.OperatorPrefixAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.OperatorPrefixWrongLengthException;
import pt.ist.anacom.shared.exception.PhoneAlreadyExistsException;
import pt.ist.anacom.shared.exception.PhoneAndOperatorPrefixDoNotMatchException;
import pt.ist.anacom.shared.exception.PhoneNumberWrongLengthException;
import pt.ist.anacom.shared.exception.PhoneStateException;
import pt.ist.anacom.shared.exception.SMSInvalidTextException;
import pt.ist.anacom.shared.stubs.server.AnacomPortType;
import pt.ist.anacom.shared.stubs.server.AnacomService;

public class ApplicationServerBridgeDistSoft implements ApplicationServerBridge {

    private AnacomPortType getPort(String operatorPrefix) {

        AnacomService anacom = new AnacomService();
        AnacomPortType port = anacom.getAnacomPort();

        String endpointURL = "http://localhost:8080/" + operatorPrefix + "/" + operatorPrefix;
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

        return port;
    }

    public String getOperatorPrefix(String phoneNumber) {
        return phoneNumber.substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
    }

    @Override
    public void cleanDomain(OperatorSimpleDto operatorDto) {
        FrontEnd fe = new FrontEnd();
        fe.cleanDomain(operatorDto);
    }

    @Override
    public void registerOperator(OperatorDetailedDto operatorDto) throws OperatorPrefixAlreadyExistsException,
            OperatorPrefixWrongLengthException,
            OperatorNullNameException,
            InvalidTaxException,
            OperatorNameAlreadyExistsException {
        FrontEnd fe = new FrontEnd();
        fe.registerOperator(operatorDto);
    }

    @Override
    public void registerPhone(PhoneDetailedDto phoneDto) throws PhoneAlreadyExistsException,
            PhoneAndOperatorPrefixDoNotMatchException,
            OperatorPrefixDoesNotExistException,
            PhoneNumberWrongLengthException {
        FrontEnd fe = new FrontEnd();
        fe.registerPhone(phoneDto);
    }

    @Override
    public void cancelRegisteredPhone(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        FrontEnd fe = new FrontEnd();
        fe.cancelRegisteredPhone(phoneDto);
    }

    @Override
    public void increasePhoneBalance(BalanceAndPhoneDto incBalDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            BalanceLimitExceededException {
        FrontEnd fe = new FrontEnd();
        fe.increasePhoneBalance(incBalDto);
    }

    @Override
    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        FrontEnd fe = new FrontEnd();
        return fe.getPhoneBalance(phoneDto);
    }

    @Override
    public StateDto getPhoneState(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {

        FrontEnd fe = new FrontEnd();
        return fe.getPhoneState(phoneDto);
    }

    @Override
    public void setPhoneState(PhoneAndStateDto setStateDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException {
        FrontEnd fe = new FrontEnd();
        fe.setPhoneState(setStateDto);
    }

    @Override
    public LastCommunicationDto getPhoneLastMadeCommunication(PhoneSimpleDto phoneDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NoCommunicationsMadeYetException {
        FrontEnd fe = new FrontEnd();
        return fe.getPhoneLastMadeCommunication(phoneDto);
    }

    @Override
    public BalanceAndPhoneListDto getBalanceAndPhoneList(OperatorSimpleDto operatorDto) throws OperatorPrefixDoesNotExistException {
        FrontEnd fe = new FrontEnd();
        return fe.getBalanceAndPhoneList(operatorDto);
    }

    @Override
    public void sendSMS(SMSDto smsDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            SMSInvalidTextException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            PhoneStateException {
        FrontEnd fe = new FrontEnd();
        fe.sendSMS(smsDto);
    }

    @Override
    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException {
        FrontEnd fe = new FrontEnd();
        return fe.getSMSPhoneReceivedList(phoneDto);
    }

    @Override
    public void startVoiceCall(CommunicationDto voiceDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            PhoneStateException {

        FrontEnd fe = new FrontEnd();
        fe.startVoiceCall(voiceDto);
    }

    @Override
    public void endVoiceCall(CommunicationDurationDto voiceDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            InvalidDurationException,
            NoActiveCommunicationException {

        FrontEnd fe = new FrontEnd();
        fe.endVoiceCall(voiceDto);
    }
}
