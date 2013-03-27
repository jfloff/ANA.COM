package pt.ist.anacom.service.bridge;

import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.OperatorDto;
import pt.ist.anacom.shared.dto.PhonesAndBalanceListDto;
import pt.ist.anacom.shared.dto.PhoneDto;
import pt.ist.anacom.shared.dto.PhoneReceivedSMSListDto;
import pt.ist.anacom.shared.dto.SMSDto;

public interface ApplicationServerBridge {

	public void registerPhone(PhoneDto dto);

	public void registerOperator(OperatorDto operatorDto);

	public void sendSMS(SMSDto SMSDto);

	public void increaseBalance(BalanceDto balDto);

	public void getPhoneBalance(BalanceDto getBalDto);

	public void getPhonesBalanceList(PhonesAndBalanceListDto dto);

	public void cancelRegisteredPhone(PhoneDto dto);

	public void getPhoneReceivedSMSList(PhoneReceivedSMSListDto dto);

}