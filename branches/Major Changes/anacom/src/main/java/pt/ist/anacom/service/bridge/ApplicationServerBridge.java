package pt.ist.anacom.service.bridge;

import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationOutDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateAndPhoneDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.dto.VoiceCallDto;
import pt.ist.anacom.shared.dto.VoiceEndCallDto;

public interface ApplicationServerBridge {

    public void registerPhone(PhoneDetailedDto phoneDto);

    public void registerOperator(OperatorDetailedDto operatorDto);

    public BalanceDto getPhoneBalance(PhoneSimpleDto phoneDto);

    public void increaseBalance(BalanceAndPhoneDto incBalDto);

    public void sendSMS(SMSDto SMSDto);

    public BalanceAndPhoneListDto getBalanceAndPhoneList(OperatorSimpleDto operatorDto);

    public void cancelRegisteredPhone(PhoneSimpleDto phoneDto);

    public SMSPhoneReceivedListDto getSMSPhoneReceivedList(PhoneSimpleDto phoneDto);

    public StateDto getPhoneState(PhoneSimpleDto phoneDto);

    public void setPhoneState(StateAndPhoneDto setStateDto);

    public CommunicationOutDto getLastMadeCommunication(PhoneSimpleDto phoneDto);

	public void startVoiceCall(VoiceCallDto voiceCallDto);

	public void endVoiceCall(VoiceEndCallDto voiceEndCallDto);
}
