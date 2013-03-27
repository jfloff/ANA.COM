package pt.ist.anacom.presentationserver;

import java.util.List;

import pt.ist.anacom.applicationserver.DatabaseBootstrap;
import pt.ist.anacom.presentationserver.server.ApplicationServerBridgeDistSoft;
import pt.ist.anacom.presentationserver.server.ApplicationServerBridgeSoftEng;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.StateAndPhoneDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.OperatorException;
import pt.ist.anacom.shared.exception.PhoneException;

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

        // criar operadores
        registerOperatorCommand("98", "SDES", tax, taxVoice, taxSMS, taxVideo);
        registerOperatorCommand("96", "TMN", tax, taxVoice, taxSMS, taxVideo);

        // criar telefones
        registerPhoneCommand("98", "981111111", AnacomData.PhoneType.GEN3);
        registerPhoneCommand("98", "982222222", AnacomData.PhoneType.GEN3);
        registerPhoneCommand("98", "963333333", AnacomData.PhoneType.GEN2);
        registerPhoneCommand("98", "964444444", AnacomData.PhoneType.GEN3);

        /*
         * 
         * // alterar estados System.out.println("Current State = " +
         * getPhoneStateCommand("981111111").getState().name());
         * setPhoneStateCommand("981111111", AnacomData.State.BUSY);
         * System.out.println("Current State = " +
         * getPhoneStateCommand("981111111").getState().name());
         * setPhoneStateCommand("981111111", AnacomData.State.SILENCE);
         * System.out.println("Current State = " +
         * getPhoneStateCommand("981111111").getState().name());
         * setPhoneStateCommand("982222222", AnacomData.State.ON);
         * 
         * // testar get balances System.out.println("98 Balances = " +
         * getBalanceAndPhoneListCommand("98").toString());
         * 
         * // alterar balancos System.out.println("Balance1 = " +
         * getPhoneBalanceCommand("981111111").getBalance());
         * increasePhoneBalanceCommand("981111111", 10); System.out.println("Balance1 = "
         * + getPhoneBalanceCommand("981111111").getBalance());
         * increasePhoneBalanceCommand("982222222", 10); System.out.println("Balance2 = "
         * + getPhoneBalanceCommand("982222222").getBalance());
         * 
         * // testar get balances System.out.println("98 Balances = " +
         * getBalanceAndPhoneListCommand("98").getPhoneList().toString());
         * 
         * // testar enviar SMS sendSMSCommand("981111111", "982222222", "Ola tudo bem?");
         * sendSMSCommand("982222222", "981111111", "Tudo! Jantar logo a noite?");
         * sendSMSCommand("981111111", "982222222", "Ya deal!");
         * 
         * // testar getReceivedPhoneList System.out.println("SMS2 List = " +
         * getPhoneReceivedSMSListCommand("981111111").getSmsList().toString());
         * System.out.println("SMS2 List = " +
         * getPhoneReceivedSMSListCommand("982222222").getSmsList().toString());
         * 
         * // testar cancel cancelRegisterCommand("981111111");
         * cancelRegisterCommand("981111111");
         */
    }

    private static void registerOperatorCommand(String operatorPefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo) {
        try {
            // TESTED
            OperatorDetailedDto operatorDto = new OperatorDetailedDto(operatorPefix, name, tax, taxVoice, taxSMS, taxVideo);
            serviceBridge.registerOperator(operatorDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void registerPhoneCommand(String operatorPrefix, String phoneNumber, AnacomData.PhoneType phoneGen) {
        try {
            // TESTED
            PhoneDetailedDto phoneDto = new PhoneDetailedDto(operatorPrefix, phoneNumber, phoneGen);
            serviceBridge.registerPhone(phoneDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
    }

    private static StateDto getPhoneStateCommand(String phoneNumber) {
        try {
            // TESTED
            PhoneSimpleDto phoneDto = new PhoneSimpleDto(phoneNumber);
            return serviceBridge.getPhoneState(phoneDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static void setPhoneStateCommand(String phoneNumber, AnacomData.State state) {
        try {
            // TESTED
            StateAndPhoneDto phoneStateDto = new StateAndPhoneDto(phoneNumber, state);
            serviceBridge.setPhoneState(phoneStateDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
    }

    private static BalanceDto getPhoneBalanceCommand(String phoneNumber) {
        try {
            // TESTED
            PhoneSimpleDto phoneDto = new PhoneSimpleDto(phoneNumber);
            return serviceBridge.getPhoneBalance(phoneDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static void increasePhoneBalanceCommand(String phoneNumber, int value) {
        try {
            // TESTED
            BalanceAndPhoneDto incBalDto = new BalanceAndPhoneDto(phoneNumber, value);
            serviceBridge.increaseBalance(incBalDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
    }

    private static BalanceAndPhoneListDto getBalanceAndPhoneListCommand(String operatorPrefix) {
        try {
            OperatorSimpleDto operatorDto = new OperatorSimpleDto(operatorPrefix);
            return serviceBridge.getBalanceAndPhoneList(operatorDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static void sendSMSCommand(String sourcePhone, String destinationPhone, String text) {
        try {
            SMSDto SMSDto = new SMSDto(sourcePhone, destinationPhone, text);
            serviceBridge.sendSMS(SMSDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
    }

    private static SMSPhoneReceivedListDto getPhoneReceivedSMSListCommand(String phoneNumber) {
        try {
            PhoneSimpleDto phoneDto = new PhoneSimpleDto(phoneNumber);
            return serviceBridge.getSMSPhoneReceivedList(phoneDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static void cancelRegisterCommand(String phoneNumber) {

        try {
            // TESTED
            PhoneSimpleDto dto = new PhoneSimpleDto(phoneNumber);
            serviceBridge.cancelRegisteredPhone(dto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
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
