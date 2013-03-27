package pt.ist.anacom.presentationserver;

import pt.ist.anacom.applicationserver.DatabaseBootstrap;
import pt.ist.anacom.presentationserver.server.ApplicationServerBridgeDistSoft;
import pt.ist.anacom.presentationserver.server.ApplicationServerBridgeSoftEng;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.data.AnacomData;
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
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorException;
import pt.ist.anacom.shared.exception.PhoneException;

public class PresentationServer {

    public static ApplicationServerBridge serviceBridge = null;
    long startTime;

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

        // criar operadores
        registerOperatorCommand("91", "RED", 20, 20, 5, 50, 0);
        registerOperatorCommand("93", "ORANGE", 50, 25, 10, 40, 0);

        // criar telefones
        registerPhoneCommand("91", "911111111", AnacomData.PhoneType.GEN3);
        registerPhoneCommand("91", "911111112", AnacomData.PhoneType.GEN3);
        registerPhoneCommand("93", "931111111", AnacomData.PhoneType.GEN3);

        increasePhoneBalanceCommand("911111111", 10000);
        setPhoneStateCommand("911111111", AnacomData.State.OFF);
        setPhoneStateCommand("911111112", AnacomData.State.ON);
        setPhoneStateCommand("931111111", AnacomData.State.ON);

        sendSMSCommand("911111111", "931111111", "teste");

        System.out.println(getLastMadeCommunication("911111111").toString());
        //
        // // Testing
        // startVoiceCallCommand("911111111", "931111111");
        //
        // System.out.println("b4 sleep");
        // try {
        // Thread.sleep(4400);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // System.out.println("after sleep");
        //
        // endVoiceCallCommand("911111111", "931111111");
        //
        // System.out.println(getLastMadeCommunication("911111111").toString());
        // System.out.println("Finish!");

    }

    private void endVoiceCallCommand(String sourcePhoneNumber, String destinationPhoneNumber) {
        try {
            int duration = ((int) (System.currentTimeMillis() - startTime)) / 1000;
            VoiceEndCallDto voiceEndCallDto = new VoiceEndCallDto(sourcePhoneNumber, destinationPhoneNumber, duration);
            serviceBridge.endVoiceCall(voiceEndCallDto);
        } catch (NoSuchPhoneException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
    }

    private void startVoiceCallCommand(String sourcePhoneNumber, String destinationPhoneNumber) {
        try {
            startTime = System.currentTimeMillis();
            VoiceCallDto voiceCallDto = new VoiceCallDto(sourcePhoneNumber, destinationPhoneNumber);
            serviceBridge.startVoiceCall(voiceCallDto);
        } catch (NoSuchPhoneException e) {
            System.err.println(e.getMessage());
        } catch (InsuficientBalanceException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
    }

    // Done

    private static CommunicationOutDto getLastMadeCommunication(String phoneNumber) {
        try {
            PhoneSimpleDto phoneDto = new PhoneSimpleDto(phoneNumber);
            return serviceBridge.getLastMadeCommunication(phoneDto);
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }


    private static void registerOperatorCommand(String operatorPefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo, int taxBonus) {
        try {
            // TESTED
            OperatorDetailedDto operatorDto = new OperatorDetailedDto(operatorPefix, name, tax, taxVoice, taxSMS, taxVideo, taxBonus);
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
