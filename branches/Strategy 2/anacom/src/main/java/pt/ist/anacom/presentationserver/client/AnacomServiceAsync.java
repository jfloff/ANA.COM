package pt.ist.anacom.presentationserver.client;

import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.BalanceLimitExceededException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidDurationException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneStateException;
import pt.ist.anacom.shared.exception.SMSInvalidTextException;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AnacomServiceAsync {

    void initBridge(String serverType, AsyncCallback<Void> callback);

    void getPhoneBalance(PhoneSimpleDto phoneDto, AsyncCallback<BalanceDto> callback) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    void increaseBalance(BalanceAndPhoneDto incBalDto, AsyncCallback<Void> callback) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, NegativeBalanceValueException, BalanceLimitExceededException;

    void getPhoneState(PhoneSimpleDto phoneDto, AsyncCallback<StateDto> callback) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    void setPhoneState(PhoneAndStateDto setStateDto, AsyncCallback<Void> callback) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    void sendSMS(SMSDto SMSDto, AsyncCallback<Void> callback) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, SMSInvalidTextException, NegativeBalanceValueException, InsuficientBalanceException, PhoneStateException;

    void getSMSPhoneReceivedList(PhoneSimpleDto phoneDto, AsyncCallback<SMSPhoneReceivedListDto> callback) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    void getLastMadeCommunication(PhoneSimpleDto phoneDto, AsyncCallback<LastCommunicationDto> callback) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, NoCommunicationsMadeYetException;

    void startVoiceCall(CommunicationDto voiceCallDto, AsyncCallback<Void> callback) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, NegativeBalanceValueException, InsuficientBalanceException, PhoneStateException;

    void endVoiceCall(CommunicationDurationDto voiceEndCallDto, AsyncCallback<Void> callback) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException, NegativeBalanceValueException, InsuficientBalanceException, InvalidDurationException, NoActiveCommunicationException;

}
