package pt.ist.anacom.service.test;

import pt.ist.anacom.service.ProcessReceiveSMSService;
import pt.ist.anacom.service.ProcessSendSMSService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.PhoneException;

public class SendSMSServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_NAME = "SDES";
    private static String OPERATOR_PREFIX = "98";
    private static String SMS_NUMBER_SEND_ON = "982222222";
    private static String SMS_NUMBER_RECEIVE_ON = "981111111";
    private static String SMS_NUMBER_SEND_OFF = "983333333";
    private static String SMS_NUMBER_RECEIVE_OFF = "985555555";
    private static String SMS_NUMBER_SEND_BUSY = "984444444";
    private static String SMS_NUMBER_NOT_RECEIVE = "983456789";
    private static String SMS_NUMBER_WITHOUT_BALANCE = "986666666";
    private static String SMS_MESSAGE = "Hello";
    private static String SMS_MESSAGE_OFF = "SENDER OR RECEIVER PHONE IS OFF";
    private static String SMS_MESSAGE_NOT_EXIST = "SENDER PHONE DOES NOT EXIST";
    private static String SMS_MESSAGE_DIFF = "Bye";
    private static int PHONE_BALANCE = 100;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;
    private static AnacomData.PhoneType GEN_3G = AnacomData.PhoneType.GEN3;
    private static AnacomData.State STATE_ON = AnacomData.State.ON;
    private static AnacomData.State STATE_OFF = AnacomData.State.OFF;
    private static AnacomData.State STATE_BUSY = AnacomData.State.BUSY;
    private static AnacomData.State STATE_SILENCE = AnacomData.State.SILENCE;

    public SendSMSServiceTest(String msg) {
        super(msg);
    }

    public SendSMSServiceTest() {
        super();
    }

    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_SEND_ON, PHONE_BALANCE, GEN_3G, STATE_ON);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_RECEIVE_ON, PHONE_BALANCE, GEN_3G, STATE_ON);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_SEND_OFF, PHONE_BALANCE, GEN_3G, STATE_OFF);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_RECEIVE_OFF, PHONE_BALANCE, GEN_3G, STATE_OFF);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_SEND_BUSY, PHONE_BALANCE, GEN_3G, STATE_BUSY);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_WITHOUT_BALANCE, 0, GEN_3G, STATE_ON);
    }

    public void testSendSMSServiceTest() {

        // Arrange
        SMSDto dto = new SMSDto(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE);
        ProcessSendSMSService sendService = new ProcessSendSMSService(dto);
        ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(dto);

        // Act
        try {
            sendService.execute();
            receiveService.execute();
        } catch (AnacomException e) {
            fail("SMS does not exist yet. " + "Should be added without throwing an exception.");
        }

        // Assert
        assertTrue("SMS Exists ", checkSMS(SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE));
        assertFalse("SMS not exists", checkSMS(SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE_DIFF));
        assertTrue("SMS Exists ", checkSMS(SMS_NUMBER_SEND_ON, SMS_MESSAGE));
        assertFalse("SMS not exists", checkSMS(SMS_NUMBER_SEND_ON, SMS_MESSAGE_DIFF));
    }

    public void testSendSMSNULLTextService() {

        // Arrange
        SMSDto dto = new SMSDto(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_ON, null);
        ProcessSendSMSService sendService = new ProcessSendSMSService(dto);
        ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(dto);

        // Act
        try {
            sendService.execute();
            receiveService.execute();
            fail("SMS was send. Should throw an exception.");
        } catch (AnacomException e) {

            System.err.println(e.getMessage());
        }
    }

    public void testSendSMSSenderPhoneOFFService() {

        // Arrange
        SMSDto dto = new SMSDto(SMS_NUMBER_SEND_OFF, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE_OFF);
        ProcessSendSMSService sendService = new ProcessSendSMSService(dto);
        ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(dto);

        // Act
        try {
            sendService.execute();
            receiveService.execute();
            fail("SMS was send. Should throw an exception.");
        } catch (PhoneException e) {

            System.err.println(e.getMessage());
        }
    }

    public void testSendSMSReceiverPhoneOFFService() {

        // Arrange
        SMSDto dto = new SMSDto(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_OFF, SMS_MESSAGE_OFF);
        ProcessSendSMSService sendService = new ProcessSendSMSService(dto);
        ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(dto);

        // Act
        try {
            sendService.execute();
            receiveService.execute();
            fail("SMS was send. Should throw an exception.");
        } catch (PhoneException e) {

            System.err.println(e.getMessage());
        }
    }

    public void testSendSMSReceiverPhoneBUSYService() {

        // Arrange
        SMSDto dto = new SMSDto(SMS_NUMBER_SEND_BUSY, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE_OFF);
        ProcessSendSMSService sendService = new ProcessSendSMSService(dto);
        ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(dto);

        // Act
        try {
            sendService.execute();
            receiveService.execute();
            fail("SMS was send. Should throw an exception.");
        } catch (PhoneException e) {

            System.err.println(e.getMessage());
        }
    }

    public void testSendSMSReceiverPhoneNotExistService() {

        // Arrange
        SMSDto dto = new SMSDto(SMS_NUMBER_SEND_ON, SMS_NUMBER_NOT_RECEIVE, SMS_MESSAGE_NOT_EXIST);
        ProcessSendSMSService sendService = new ProcessSendSMSService(dto);
        ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(dto);

        // Act
        try {
            sendService.execute();
            receiveService.execute();
            fail("SMS was send. Should throw an exception.");
        } catch (PhoneException e) {

            System.err.println(e.getMessage());
        }
    }

    public void testSendSMSReceiverInsuficientBalanceService() {

        // Arrange
        SMSDto dto = new SMSDto(SMS_NUMBER_WITHOUT_BALANCE, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE);
        ProcessSendSMSService sendService = new ProcessSendSMSService(dto);
        ProcessReceiveSMSService receiveService = new ProcessReceiveSMSService(dto);

        // Act
        try {
            sendService.execute();
            receiveService.execute();
            fail("SMS was send. Should throw an exception.");
        } catch (PhoneException e) {

            System.err.println(e.getMessage());
        }
    }
}
