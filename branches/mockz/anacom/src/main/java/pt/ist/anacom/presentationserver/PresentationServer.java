package pt.ist.anacom.presentationserver;

import java.security.KeyPair;
import java.util.Properties;

import org.apache.log4j.Logger;

import pt.ist.anacom.applicationserver.DatabaseBootstrap;
import pt.ist.anacom.presentationserver.server.ApplicationServerBridgeDistSoft;
import pt.ist.anacom.presentationserver.server.ApplicationServerBridgeSoftEng;
import pt.ist.anacom.service.bridge.ApplicationServerBridge;
import pt.ist.anacom.shared.data.AnacomData;
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
import pt.ist.anacom.shared.exception.OperatorException;
import pt.ist.anacom.shared.exception.PhoneException;
import pt.ist.anacom.shared.security.EntityINFO;
import pt.ist.ca.server.CertificateAuthorityServiceBridge;
import pt.ist.ca.service.bridge.CertificateAuthorityBridge;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.OperatorCertificateInfoDto;
import pt.ist.ca.shared.dto.PublicKeyDto;
import pt.ist.shared.CertificateContents;
import pt.ist.shared.SecurityData;
import pt.ist.shared.SignedCertificate;

public class PresentationServer {

    public static ApplicationServerBridge serviceBridge = null;
    long startTime;

    /**
     * All attributes necessary for easy access on handler side.
     */
    public static CertificateAuthorityBridge certificateAuthority;
    public static KeyPair keyPair;
    public static EntityINFO clientINFO;

    public void init(String server) {

        Logger.getLogger(this.getClass()).info("-------------------- PRESENTATION SERVER -------------------- ");

        clientINFO = new EntityINFO(server, "build.properties");
        certificateAuthority = new CertificateAuthorityServiceBridge(clientINFO);

        if (server.equals("ES+SD")) {

            serviceBridge = new ApplicationServerBridgeDistSoft();

        } else if (server.equals("ES-only")) {

            serviceBridge = new ApplicationServerBridgeSoftEng();
            DatabaseBootstrap.init();

        } else {

            throw new RuntimeException("Servlet parameter error: ES+SD nor ES-only");
        }

        try {
            Properties prop = SecurityData.readPropertiesFile("build.properties");
            String pubPath = prop.getProperty("publicKeyPath") + clientINFO.getEntityID() + "public.dat";
            String privPath = prop.getProperty("privateKeyPath") + clientINFO.getEntityID() + "private.dat";
            keyPair = SecurityData.generateKeys("RSA", 1024);
            SecurityData.writeKeys(keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded(), pubPath, privPath);
            String publicKey = SecurityData.encode64(keyPair.getPublic().getEncoded());

            OperatorCertificateInfoDto operatorCertificateDto = new OperatorCertificateInfoDto(clientINFO.getEntityID(), publicKey,
                    SecurityData.VALIDITY);
            CertificateDto dto = certificateAuthority.signCertificate(operatorCertificateDto);
            String sig = dto.getSignature();
            String certContents = dto.getCertificate();
            CertificateContents contents = (CertificateContents) SecurityData.deserialize(SecurityData.decode64(certContents));
            SignedCertificate actualCertificate = new SignedCertificate(contents, sig);

            clientINFO.setActualCertificate(actualCertificate);

            PublicKeyDto pk = certificateAuthority.getCAPublicKey();
            String caPublicKey = pk.getPublicKey();
            clientINFO.setCaPublicKey(caPublicKey);

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
        registerPhoneCommand("91", "912222222", AnacomData.PhoneType.GEN3);
        Logger.getLogger(this.getClass()).info("Write registerOperatorCommand success");

//        setPhoneStateCommand("911111111", AnacomData.State.ON);
//        setPhoneStateCommand("912222222", AnacomData.State.SILENCE);
//        Logger.getLogger(this.getClass()).info("Write setPhoneStateCommand success");
//
//        System.out.println("PHONE 912222222 " + "WITH STATE: "+ getPhoneStateCommand("912222222"));
//        Logger.getLogger(this.getClass()).info("Read getPhoneStateCommand success");

        increasePhoneBalanceCommand("911111111", 5000);
        Logger.getLogger(this.getClass()).info("Write increasePhoneBalanceCommand success");

        increasePhoneBalanceCommand("912222222", 3587);
        Logger.getLogger(this.getClass()).info("Write increasePhoneBalanceCommand success");
        
//        System.out.println("Balance -> " + getPhoneBalanceCommand("911111111"));
//        Logger.getLogger(this.getClass()).info("Write getPhoneBalanceCommand success");
//        
//        sendSMSCommand("911111111", "912222222", "Ó filho... molha o milho no molho");
//        Logger.getLogger(this.getClass()).info("Write sendSMSCommand success");
//        
//        sendSMSCommand("911111111", "912222222", "O edgar é bué BRUTO!");
//        Logger.getLogger(this.getClass()).info("Write sendSMSCommand success");
//
//        
//        System.out.println("LAST COMMUNICATION: " + getLastMadeCommunicationCommand("911111111").toString());
//        Logger.getLogger(this.getClass()).info("Read lastCommunicationCommand success");
//        
//        System.out.println("SMS LIST RECEIVED: " + getSMSPhoneReceivedListCommand("912222222"));
//        Logger.getLogger(this.getClass()).info("Read smsPhoneReceivedListCommand success");
        
        System.out.println("BALANCE AND PHONES LIST: " + getBalanceAndPhoneListCommand("91").toString());
        Logger.getLogger(this.getClass()).info("Read balanceAndPhoneListCommand success");

        

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
        System.out.println("Finish!");
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

    private static SMSPhoneReceivedListDto getSMSPhoneReceivedListCommand(String phoneNumber) {
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

    private static LastCommunicationDto getLastMadeCommunicationCommand(String phoneNumber) {
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
        } catch (PhoneException e) {
            System.err.println(e.getMessage());
        } catch (OperatorException e) {
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
        String server = System.getProperty("server.type", "ES+SD");
        ps.init(server);
        ps.ourMain();
    }
}
