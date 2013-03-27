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
import pt.ist.anacom.shared.exception.CouldNotSetStateWhileCommunicationActiveException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.InvalidDurationException;
import pt.ist.anacom.shared.exception.NegativeBalanceValueException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoCommunicationsMadeYetException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneStateException;
import pt.ist.anacom.shared.exception.SMSInvalidTextException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service")
public interface AnacomService extends RemoteService {

    public void initBridge(String serverType);

    BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    void increaseBalance(BalanceAndPhoneDto incBalDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            BalanceLimitExceededException;

    StateDto getPhoneState(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    void setPhoneState(PhoneAndStateDto setStateDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException, CouldNotSetStateWhileCommunicationActiveException;

    void sendSMS(SMSDto SMSDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            SMSInvalidTextException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            PhoneStateException;

    SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    LastCommunicationDto getLastMadeCommunication(PhoneSimpleDto phoneDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NoCommunicationsMadeYetException;

    public void startVoiceCall(CommunicationDto voiceCallDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            PhoneStateException;

    public void endVoiceCall(CommunicationDurationDto voiceEndCallDto) throws NoSuchPhoneException,
            OperatorPrefixDoesNotExistException,
            NegativeBalanceValueException,
            InsuficientBalanceException,
            InvalidDurationException,
            NoActiveCommunicationException;

}
