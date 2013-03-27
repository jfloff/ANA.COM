package pt.ist.anacom.presentationserver;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.apache.log4j.Logger;

import pt.ist.anacom.applicationserver.DatabaseBootstrap;
import pt.ist.anacom.presentationserver.server.ApplicationServerBridgeDistSoft;
import pt.ist.anacom.presentationserver.server.ApplicationServerBridgeSoftEng;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.data.Data;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.BalanceDto;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.dto.LastCommunicationDto;
import pt.ist.anacom.shared.dto.OperatorDetailedDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.dto.StateDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.InsuficientBalanceException;
import pt.ist.anacom.shared.exception.NoActiveCommunicationException;
import pt.ist.anacom.shared.exception.NoSuchPhoneException;
import pt.ist.anacom.shared.exception.OperatorException;
import pt.ist.anacom.shared.exception.PhoneException;

public class PresentationServer {

    public static ApplicationServerBridge serviceBridge = null;
    long startTime;
    public static KeyPair keyPair;


    public void init(String server) {

        Logger.getLogger(this.getClass()).info("-------------------- PRESENTATION SERVER -------------------- ");

        if (server.equals("ES+SD")) {

            serviceBridge = new ApplicationServerBridgeDistSoft();

        } else if (server.equals("ES-only")) {

            serviceBridge = new ApplicationServerBridgeSoftEng();
            DatabaseBootstrap.init();

        } else {

            throw new RuntimeException("Servlet parameter error: ES+SD nor ES-only");
        }

        try {
            Properties prop = Data.readPropertiesFile("build.properties");
            String pubPath = prop.getProperty("publicKeyPath") + "clientpublic.txt";
            String privPath = prop.getProperty("privateKeyPath") + "clientprivate.txt";
            keyPair = Data.generateKeys("RSA", 1024);
            Data.writeKeys(keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded(), pubPath, privPath);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public ApplicationServerBridge getBridge() {
        return serviceBridge;
    }

    public void ourMain() {

        cleanDomainCommand("91");

        registerOperatorCommand("91", "Vodafone", 20, 20, 5, 50, 2);
        Logger.getLogger(this.getClass()).info("Write registerOperatorCommand success");

        registerPhoneCommand("91", "911111111", AnacomData.PhoneType.GEN3);
        Logger.getLogger(this.getClass()).info("Write registerPhoneCommand success");

        registerPhoneCommand("91", "912222222", AnacomData.PhoneType.GEN3);
        Logger.getLogger(this.getClass()).info("Write registerPhoneCommand success");

        increasePhoneBalanceCommand("911111111", 9000);
        Logger.getLogger(this.getClass()).info("Write increasePhoneBalanceCommand success");

        System.out.println("BALANCE: " + getPhoneBalanceCommand("911111111"));
        Logger.getLogger(this.getClass()).info("Get getPhoneBalanceCommand success");

        setPhoneStateCommand("911111111", AnacomData.State.ON);
        Logger.getLogger(this.getClass()).info("Write setPhoneStateCommand success");
        setPhoneStateCommand("912222222", AnacomData.State.ON);
        Logger.getLogger(this.getClass()).info("Write setPhoneStateCommand success");

        sendSMSCommand("911111111", "912222222", "Avozinho");
        Logger.getLogger(this.getClass()).info("Write sendSMSCommand success");

        // startVoiceCallCommand("911111111", "912222222");
        //
        // System.out.println("[STARTING SIMULATING VOICE CALL]");
        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // }
        //
        // sendSMSCommand("911111111", "912222222", "Avozinho");
        //
        // endVoiceCallCommand("911111111", "912222222");
        // System.out.println("[ENDING SIMULATING VOICE CALL]");

        //
        // System.out.println(getPhoneBalanceCommand("911111111"));
        // increasePhoneBalanceCommand("911111111", 9000);
        // Logger.getLogger(this.getClass()).info("Write success");
        //
        //
        // // System.out.println(getPhoneStateCommand("911111111"));
        // // System.out.println(getPhoneStateCommand("912222222"));
        // // System.out.println(getLastMadeCommunication("911111111"));
        //
        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // }
        //
        //

        //
        // System.out.println(getPhoneBalanceCommand("911111111"));
        // //
        // // System.out.println(getPhoneStateCommand("911111111"));
        // // System.out.println(getPhoneStateCommand("912222222"));
        // //
        // // System.out.println(getLastMadeCommunication("911111111"));
        // System.out.println("Finish!");


        // /* CLEANS PARA SD */
        // cleanDomainCommand("91-1");
        // cleanDomainCommand("93");
        // cleanDomainCommand("96");
        //
        // // criar operadores
        // registerOperatorCommand("91", "Vodafone", 20, 20, 5, 50, 2);
        // registerOperatorCommand("93", "ORANGE", 50, 25, 10, 40, 1);
        //
        // // criar telefones
        // registerPhoneCommand("91", "911111111", AnacomData.PhoneType.GEN3);
        // registerPhoneCommand("91", "911111112", AnacomData.PhoneType.GEN3);
        // registerPhoneCommand("93", "931111111", AnacomData.PhoneType.GEN3);
        //
        // increasePhoneBalanceCommand("911111111", 5000);
        // increasePhoneBalanceCommand("931111111", 5000);
        //
        // setPhoneStateCommand("911111111", AnacomData.State.SILENCE);
        // setPhoneStateCommand("911111112", AnacomData.State.ON);
        // setPhoneStateCommand("931111111", AnacomData.State.ON);
        //
        // System.out.println(getPhoneStateCommand("911111111"));
        // System.out.println(getPhoneStateCommand("931111111"));
        //
        // // Testing
        // startVoiceCallCommand("911111111", "931111111");
        //
        // System.out.println("[STARTING SIMULATING VOICE CALL]");
        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // }
        //
        // sendSMSCommand("911111111", "911111112", "Avozinho");
        // System.out.println(getPhoneStateCommand("911111111"));
        // System.out.println(getPhoneStateCommand("931111111"));
        // System.out.println(getLastMadeCommunication("911111111"));
        //
        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // }
        //
        // endVoiceCallCommand("911111111", "911111112");
        //
        // endVoiceCallCommand("911111111", "931111111");
        // System.out.println("[ENDING SIMULATING VOICE CALL]");
        //
        //
        // System.out.println(getPhoneStateCommand("911111111"));
        // System.out.println(getPhoneStateCommand("931111111"));
        //
        // System.out.println(getLastMadeCommunication("911111111"));
        // System.out.println("Finish!");
    }

    private void cleanDomainCommand(String operatorPrefix) {
        OperatorSimpleDto operatorDto = new OperatorSimpleDto(operatorPrefix);
        serviceBridge.cleanDomain(operatorDto);
    }

    private static void registerOperatorCommand(String operatorPefix, String name, int tax, int taxVoice, int taxSMS, int taxVideo, int taxBonus) {
        try {
            // TESTED
            OperatorDetailedDto operatorDto = new OperatorDetailedDto(operatorPefix, name, tax, taxVoice, taxSMS, taxVideo, taxBonus);
            serviceBridge.registerOperator(operatorDto);
        } catch (PhoneException e) {
            System.err.println("1" + e.getMessage());
        } catch (OperatorException e) {
            System.err.println("2" + e.getMessage());
        } catch (AnacomException e) {
            System.err.println("3" + e.getMessage());
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
            PhoneAndStateDto phoneStateDto = new PhoneAndStateDto(phoneNumber, state);
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
            serviceBridge.increasePhoneBalance(incBalDto);
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

    private static LastCommunicationDto getLastMadeCommunication(String phoneNumber) {
        try {
            PhoneSimpleDto phoneDto = new PhoneSimpleDto(phoneNumber);
            return serviceBridge.getPhoneLastMadeCommunication(phoneDto);
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

    private void endVoiceCallCommand(String sourcePhoneNumber, String destinationPhoneNumber) {
        try {
            int duration = AnacomData.getCommunicationDuration(startTime);
            CommunicationDurationDto voiceEndCallDto = new CommunicationDurationDto(sourcePhoneNumber, destinationPhoneNumber, duration);
            serviceBridge.endVoiceCall(voiceEndCallDto);
        } catch (NoSuchPhoneException e) {
            System.err.println(e.getMessage());
        } catch (NoActiveCommunicationException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
    }

    private void startVoiceCallCommand(String sourcePhoneNumber, String destinationPhoneNumber) {
        try {
            startTime = AnacomData.getCurrentTime();
            CommunicationDto voiceCallDto = new CommunicationDto(sourcePhoneNumber, destinationPhoneNumber);
            serviceBridge.startVoiceCall(voiceCallDto);
        } catch (NoSuchPhoneException e) {
            System.err.println(e.getMessage());
        } catch (InsuficientBalanceException e) {
            System.err.println(e.getMessage());
        } catch (AnacomException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(final String[] args) {
        PresentationServer ps = new PresentationServer();
        String server = System.getProperty("server.type", "ES+SD");
        ps.init(server);
        ps.ourMain();
    }
}
