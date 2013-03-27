package pt.ist.anacom.presentation;

import jvstm.Atomic;
import pt.ist.anacom.domain.Anacom;
import pt.ist.anacom.exception.AnacomException;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.service.bridge.LocalApplicationServer;
import pt.ist.anacom.service.dto.BalanceDto;
import pt.ist.anacom.service.dto.OperatorDto;
import pt.ist.anacom.service.dto.PhoneAndBalanceListDto;
import pt.ist.anacom.service.dto.PhoneDto;
import pt.ist.anacom.service.dto.SMSDto;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;

public class PresentationServer {

	public static ApplicationServerBridge serviceBridge = null;

	public static void main(final String[] args) {
		FenixFramework.initialize(new Config() {
			{
				domainModelPath = "src/main/dml/anacom.dml";
				dbAlias = "db/dbAnacom";
				repositoryType = RepositoryType.BERKELEYDB;
				rootClass = Anacom.class;
			}
		});

		System.out.println("Welcome to the Anacom application!");

		serviceBridge = new LocalApplicationServer();

		int tax = 4;
		int taxVoice = 1;
		int taxSMS = 2;
		int taxVideo = 5;
		registerOperatorCommand("98", "SDES", tax, taxVoice, taxSMS, taxVideo);
		registerPhoneCommand("SDES", "981111111");
		registerPhoneCommand("SDES", "981111111");
		registerPhoneCommand("SDES", "992222222");
		System.out.println(getPhonesAndBalanceCommand("SDES"));

		// int value = 10;
		// cancelRegisterCommand("SDES", "981111111");
		// increasePhoneBalanceCommand("981111111", value);
		// increasePhoneBalanceCommand("992222222", value);
		// registerOperatorCommand("911", "TRN", tax, taxVoice, taxSMS,
		// taxVideo);
		// sendSMSCommand("981111111", "982222222", "trinta");
		// printAnacom();
	}

	private static void cancelRegisterCommand(String operatorName, String phoneNumber) {

		try {
			PhoneDto dto = new PhoneDto(operatorName, phoneNumber);
			serviceBridge.cancelRegisterPhone(dto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	private static String getPhonesAndBalanceCommand(String operatorName) {
		try {
			PhoneAndBalanceListDto dto = new PhoneAndBalanceListDto(operatorName);
			serviceBridge.getPhonesBalanceDto(dto);
			return dto.toString();
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
			return "";
		}
	}

	private static int getPhoneBalanceCommand(String phoneNumber) {
		try {
			BalanceDto getBalDto = new BalanceDto(phoneNumber, 0);
			serviceBridge.getBalanceDto(getBalDto);
			return getBalDto.getBalance();
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
			return -1;
		}
	}

	private static void increasePhoneBalanceCommand(String phoneNumber, int value) {
		try {
			BalanceDto BalDto = new BalanceDto(phoneNumber, value);
			serviceBridge.increaseBalance(BalDto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void sendSMSCommand(String senderPhone, String receiverPhone, String text) {
		try {
			SMSDto SMSDto = new SMSDto(senderPhone, receiverPhone, text);
			serviceBridge.sendSMS(SMSDto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void registerPhoneCommand(String operator, String phoneNumber) {
		try {
			PhoneDto phoneDto = new PhoneDto(operator, phoneNumber);
			serviceBridge.registerPhone(phoneDto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void registerOperatorCommand(String prefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo) {
		try {
			OperatorDto operatorDto = new OperatorDto(prefix, name, tax, taxVoice, taxSMS, taxVideo);
			serviceBridge.registerOperator(operatorDto);
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}
	}

	@Atomic
	static void printAnacom() {

		Anacom anacom = FenixFramework.getRoot();
		System.out.println("========\nOperators\n========");
		anacom.printOperators();
	}
}