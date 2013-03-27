package pt.ist.anacom.presentation;

import jvstm.Atomic;
import pt.ist.anacom.applicationserver.DatabaseBootstrap;
import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.OperatorDto;
import pt.ist.anacom.shared.dto.PhoneDto;
import pt.ist.anacom.shared.dto.PhoneReceivedSMSListDto;
import pt.ist.anacom.shared.dto.PhonesAndBalanceListDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.fenixframework.FenixFramework;

public class PresentationServer {

	public static ApplicationServerBridge serviceBridge = null;

	public void init(String server) {
		if (server.equals("ES+SD")) {

			serviceBridge = new ApplicationServerBridgeDistSoft();

		} else if (server.equals("ES-only")) {
			serviceBridge = new ApplicationServerBridgeSoftEng();

			DatabaseBootstrap.init();

		} else {

			throw new RuntimeException("Servlet parameter error: ES+SD nor ES-only");
		}
	}

	static public ApplicationServerBridge getBridge() {

		return serviceBridge;
	}

	public void ourMain() {

		int tax = 4;
		int taxVoice = 1;
		int taxSMS = 2;
		int taxVideo = 5;
//		
		registerOperatorCommand("98", "SDES", tax, taxVoice, taxSMS, taxVideo);
		registerOperatorCommand("96", "TMN", tax, taxVoice, taxSMS, taxVideo);
//
		registerPhoneCommand("98", "981111111");
		registerPhoneCommand("98", "982222222");
		registerPhoneCommand("96", "963333333");
	
		increasePhoneBalanceCommand("981111111", 15);
		increasePhoneBalanceCommand("982222222", 62);
		increasePhoneBalanceCommand("963333333", 42);
		
		sendSMSCommand("981111111", "963333333", "TESTE UM");
		//System.out.println(getPhoneReceivedSMSListCommand("96", "963333333"));
		sendSMSCommand("982222222", "981111111", "TESTE DOIS");
	//	System.out.println(getPhoneReceivedSMSListCommand("98", "981111111"));
		
		System.out.println(getPhonesAndBalanceCommand("98"));
		System.out.println(getPhonesAndBalanceCommand("96"));
	}

	//3a ENTREGA ES - NAO E PARA FAZER
	private static String getPhoneReceivedSMSListCommand(String operatorPrefix, String phoneNumber) {
		try {
			//TESTED
			PhoneReceivedSMSListDto dto = new PhoneReceivedSMSListDto(operatorPrefix, phoneNumber);
			serviceBridge.getPhoneReceivedSMSList(dto);
			return dto.getSmsList().toString();
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
			return "";
		}
	}

	private static void cancelRegisterCommand(String operatorPrefix, String phoneNumber) {

		try {
			//TESTED
			PhoneDto dto = new PhoneDto(operatorPrefix, phoneNumber);
			serviceBridge.cancelRegisteredPhone(dto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	private static String getPhonesAndBalanceCommand(String operatorPrefix) {
		try {
			//TESTED
			PhonesAndBalanceListDto dto = new PhonesAndBalanceListDto(operatorPrefix);
			serviceBridge.getPhonesBalanceList(dto);
			return dto.toString();
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
			return "";
		}
	}

	private static int getPhoneBalanceCommand(String phoneNumber) {
		try {
			//TESTED
			BalanceDto balanceDto = new BalanceDto(phoneNumber, 0);
			serviceBridge.getPhoneBalance(balanceDto);
			return balanceDto.getBalance();
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
			return -1;
		}
	}

	private static void increasePhoneBalanceCommand(String phoneNumber, int value) {
		try {
			//TESTED
			BalanceDto BalDto = new BalanceDto(phoneNumber, value);
			serviceBridge.increaseBalance(BalDto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void sendSMSCommand(String senderPhone, String receiverPhone, String text) {
		try {
			//TESTED
			SMSDto SMSDto = new SMSDto(senderPhone, receiverPhone, text);
			serviceBridge.sendSMS(SMSDto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void registerPhoneCommand(String operatorPefix, String phoneNumber) {
		try {
			//TESTED
			PhoneDto phoneDto = new PhoneDto(operatorPefix, phoneNumber);
			serviceBridge.registerPhone(phoneDto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void registerOperatorCommand(String operatorPefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo) {
		try {
			//TESTED
			OperatorDto operatorDto = new OperatorDto(operatorPefix, name, tax, taxVoice, taxSMS, taxVideo);
			serviceBridge.registerOperator(operatorDto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(final String[] args) {

		PresentationServer ps = new PresentationServer();
		String server = System.getProperty("server.type", "ES-only");
		ps.init(server);
		ps.ourMain();

	}
}