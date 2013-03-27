package pt.ist.anacom.presentation;

import pt.ist.anacom.service.ProcessCancelRegisteredPhoneService;
import pt.ist.anacom.service.ProcessGetBalanceService;
import pt.ist.anacom.service.ProcessGetPhoneReceivedSMSListService;
import pt.ist.anacom.service.ProcessGetPhonesBalanceListService;
import pt.ist.anacom.service.ProcessIncBalanceService;
import pt.ist.anacom.service.ProcessReceiveSMSService;
import pt.ist.anacom.service.ProcessRegisterNewOperatorService;
import pt.ist.anacom.service.ProcessRegisterNewPhoneService;
import pt.ist.anacom.service.ProcessSendSMSService;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.OperatorDto;
import pt.ist.anacom.shared.dto.PhoneDto;
import pt.ist.anacom.shared.dto.PhoneReceivedSMSListDto;
import pt.ist.anacom.shared.dto.PhonesAndBalanceListDto;
import pt.ist.anacom.shared.dto.SMSDto;

public class ApplicationServerBridgeSoftEng implements ApplicationServerBridge {

	@Override
	public void getPhoneReceivedSMSList(PhoneReceivedSMSListDto dto) {
		ProcessGetPhoneReceivedSMSListService service = new ProcessGetPhoneReceivedSMSListService(dto);
		service.execute();
	}

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
	public void getPhoneBalance(BalanceDto balDto) {
		ProcessGetBalanceService getBalService = new ProcessGetBalanceService(balDto);
		getBalService.execute();
	}

	@Override
	public void getPhonesBalanceList(PhonesAndBalanceListDto dto) {
		ProcessGetPhonesBalanceListService getPhonesBalanceDto = new ProcessGetPhonesBalanceListService(dto);
		getPhonesBalanceDto.execute();
	}

	@Override
	public void cancelRegisteredPhone(PhoneDto dto) {
		ProcessCancelRegisteredPhoneService cancelRegister = new ProcessCancelRegisteredPhoneService(dto);
		cancelRegister.execute();
	}

}
