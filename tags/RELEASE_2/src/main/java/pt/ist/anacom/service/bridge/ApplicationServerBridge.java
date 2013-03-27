package pt.ist.anacom.service.bridge;

import pt.ist.anacom.service.dto.BalanceDto;
import pt.ist.anacom.service.dto.OperatorDto;
import pt.ist.anacom.service.dto.PhoneAndBalanceListDto;
import pt.ist.anacom.service.dto.PhoneDto;
import pt.ist.anacom.service.dto.SMSDto;

public interface ApplicationServerBridge {

	public void registerPhone(PhoneDto dto);

	public void registerOperator(OperatorDto operatorDto);

	public void sendSMS(SMSDto SMSDto);

	public void increaseBalance(BalanceDto balDto);

	public void getBalanceDto(BalanceDto getBalDto);

	public void getPhonesBalanceDto(PhoneAndBalanceListDto dto);

	public void cancelRegisterPhone(PhoneDto dto);

}