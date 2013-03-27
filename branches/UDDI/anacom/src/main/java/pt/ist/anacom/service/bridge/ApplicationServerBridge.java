package pt.ist.anacom.service.bridge;

import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.dto.VoiceCallDto;
import pt.ist.anacom.shared.dto.VoiceEndCallDto;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidTaxException;
import pt.ist.anacom.shared.exception.NegativeBalanceException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorNullNameException;
import pt.ist.anacom.shared.exception.OperatorPrefixAlreadyExistsException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.OperatorWithWrongPrefixException;
import pt.ist.anacom.shared.exception.PhoneAlreadyExistsException;
import pt.ist.anacom.shared.exception.PhoneNumberIncorrectException;
import pt.ist.anacom.shared.exception.PhonePrefixDoesNotMatchException;
import pt.ist.anacom.shared.exception.SMSInvalidTextException;

public interface ApplicationServerBridge {

    public void registerPhone(PhoneDetailedDto phoneDto) throws PhoneAlreadyExistsException,
            PhonePrefixDoesNotMatchException,
            OperatorPrefixDoesNotExistException,
            PhoneNumberIncorrectException;

    public void registerOperator(OperatorDetailedDto operatorDto) throws OperatorNullNameException,
            OperatorWithWrongPrefixException,
            OperatorPrefixAlreadyExistsException,
            InvalidTaxException;

    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException;

    public void increaseBalance(BalanceAndPhoneDto incBalDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceException,
            BalanceLimitExceededException;

    public void sendSMS(SMSDto SMSDto) throws SMSInvalidTextException,
            OperatorPrefixDoesNotExistException,
            NoSuchPhoneException,
            NegativeBalanceException,
            InsuficientBalanceException;

    public BalanceAndPhoneListDto getBalanceAndPhoneList(OperatorSimpleDto operatorDto) throws OperatorPrefixDoesNotExistException;

    public void cancelRegisteredPhone(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException;

    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    public StateDto getPhoneState(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException;

    public void setPhoneState(PhoneAndStateDto setStateDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException;

    public CommunicationDto getLastMadeCommunication(PhoneSimpleDto phoneDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NoCommunicationsMadeYetException;

    // TODO

    public void startVoiceCall(VoiceCallDto voiceCallDto);

    public void endVoiceCall(VoiceEndCallDto voiceEndCallDto);
}
