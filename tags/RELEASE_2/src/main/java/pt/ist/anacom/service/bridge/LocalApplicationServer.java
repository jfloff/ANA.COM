package pt.ist.anacom.service.bridge;

import pt.ist.anacom.service.ProcessCancelRegisterPhoneService;
import pt.ist.anacom.service.ProcessGetBalanceService;
import pt.ist.anacom.service.ProcessGetPhonesBalanceDtoService;
import pt.ist.anacom.service.ProcessIncBalanceService;
import pt.ist.anacom.service.ProcessReceiveSMSService;
import pt.ist.anacom.service.ProcessRegisterNewOperatorService;
import pt.ist.anacom.service.ProcessRegisterNewPhoneService;
import pt.ist.anacom.service.ProcessSendSMSService;
import pt.ist.anacom.service.dto.BalanceDto;
import pt.ist.anacom.service.dto.OperatorDto;
import pt.ist.anacom.service.dto.PhoneAndBalanceListDto;
import pt.ist.anacom.service.dto.PhoneDto;
import pt.ist.anacom.service.dto.SMSDto;

public class LocalApplicationServer implements ApplicationServerBridge {

	@Override
	public void registerPhone(PhoneDto dto) {
		ProcessRegisterNewPhoneService service = new ProcessRegisterNewPhoneService(dto);
		service.execute();
	}

	@Override
	public void registerOperator(OperatorDto operatorDto) {
		ProcessRegisterNewOperatorService service = new ProcessRegisterNewOperatorService(operatorDto);
		service.execute();
	}

	@Override
	public void sendSMS(SMSDto SMSDto) {
		ProcessSendSMSService sendService = new ProcessSendSMSService(SMSDto);
		ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(SMSDto);
		sendService.execute();
		receiveService.execute();
	}

	@Override
	public void increaseBalance(BalanceDto balDto) {
		ProcessIncBalanceService incBalService = new ProcessIncBalanceService(balDto);
		incBalService.execute();
	}

	@Override
	public void getBalanceDto(BalanceDto balDto) {
		ProcessGetBalanceService getBalService = new ProcessGetBalanceService(balDto);
		getBalService.execute();
	}

	@Override
	public void getPhonesBalanceDto(PhoneAndBalanceListDto dto) {
		ProcessGetPhonesBalanceDtoService getPhoneBalanceDto = new ProcessGetPhonesBalanceDtoService(dto);
		getPhoneBalanceDto.execute();
	}

	@Override
	public void cancelRegisterPhone(PhoneDto dto) {
		ProcessCancelRegisterPhoneService cancelRegister = new ProcessCancelRegisterPhoneService(dto);
		cancelRegister.execute();
	}
}
