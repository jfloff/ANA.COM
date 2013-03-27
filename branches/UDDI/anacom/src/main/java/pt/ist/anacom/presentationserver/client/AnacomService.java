package pt.ist.anacom.presentationserver.client;

import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.NegativeBalanceException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorPrefixDoesNotExistException;
import pt.ist.anacom.shared.exception.PhoneIsBUSYException;
import pt.ist.anacom.shared.exception.PhoneIsOFFException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service")
public interface AnacomService extends RemoteService {

    public void initBridge(String serverType);

    BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    void increaseBalance(BalanceAndPhoneDto incBalDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException, NegativeBalanceException;

    StateDto getPhoneState(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    void setPhoneState(PhoneAndStateDto setStateDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    void sendSMS(SMSDto SMSDto) throws OperatorPrefixDoesNotExistException,
            NoSuchPhoneException,
            InsuficientBalanceException,
            NegativeBalanceException,
            PhoneIsOFFException,
            PhoneIsBUSYException;

    SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto) throws OperatorPrefixDoesNotExistException, NoSuchPhoneException;

    CommunicationDto getLastMadeCommunication(PhoneSimpleDto phoneDto) throws NoSuchPhoneException, OperatorPrefixDoesNotExistException;
}
