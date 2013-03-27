package pt.ist.anacom.service.bridge;

import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
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

public interface ApplicationServerBridge {

    public void cleanDomain(OperatorSimpleDto operatorDto);

    public void registerOperator(OperatorDetailedDto operatorDto) throws OperatorPrefixAlreadyExistsException, OperatorPrefixWrongLengthException, OperatorNullNameException, InvalidTaxException, OperatorNameAlreadyExistsException;

    public void registerPhone(PhoneDetailedDto phoneDto) throws PhoneAlreadyExistsException, PhoneAndOperatorPrefixDoNotMatchException, OperatorPrefixDoesNotExistException, PhoneNumberWrongLengthException;

    public void cancelRegisteredPhone(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException;

    public void increasePhoneBalance(BalanceAndPhoneDto incBalDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, NegativeBalanceValueException, BalanceLimitExceededException;

    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException;

    public StateDto getPhoneState(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException;

    public void setPhoneState(PhoneAndStateDto setStateDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException;

    public LastCommunicationDto getPhoneLastMadeCommunication(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, NoCommunicationsMadeYetException;

    public BalanceAndPhoneListDto getBalanceAndPhoneList(OperatorSimpleDto operatorDto) throws OperatorPrefixDoesNotExistException;

    public void sendSMS(SMSDto SMSDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, SMSInvalidTextException, NegativeBalanceValueException, InsuficientBalanceException, PhoneStateException;

    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    public void startVoiceCall(CommunicationDto voiceCallDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, NegativeBalanceValueException, InsuficientBalanceException, PhoneStateException;

    public void endVoiceCall(CommunicationDurationDto voiceEndCallDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, NegativeBalanceValueException, InsuficientBalanceException, InvalidDurationException, NoActiveCommunicationException;

}
