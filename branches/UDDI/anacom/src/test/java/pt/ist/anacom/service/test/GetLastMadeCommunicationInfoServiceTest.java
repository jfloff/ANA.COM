package pt.ist.anacom.service.test;

import pt.ist.anacom.service.GetLastMadeCommunicationService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.OperatorException;
import pt.ist.anacom.shared.exception.PhoneException;

public class GetLastMadeCommunicationInfoServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_PREFIX = "91";
    private static String OPERATOR_NAME = "VODAFONE";
    private static String SMS_NUMBER_SEND_ON = "911111111";
    private static String VOICE_NUMBER_SEND_ON = "911111111";
    private static String SMS_NUMBER_RECEIVE_ON = "911111112";
    private static String VOICE_NUMBER_RECEIVE_ON = "911111112";
    private static String SMS_MESSAGE = "Hello";
    private static String SMS_MESSAGE_DIFF = "Bye";
    private static String SMS_MESSAGE_END = "End";
    private static int PHONE_BALANCE = 1000;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;
    private static AnacomData.PhoneType GEN_3G = AnacomData.PhoneType.GEN3;
    private static AnacomData.State STATE_ON = AnacomData.State.ON;


    public GetLastMadeCommunicationInfoServiceTest() {
        super();
    }

    public GetLastMadeCommunicationInfoServiceTest(String msg) {
        super(msg);
    }

    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_SEND_ON, PHONE_BALANCE, GEN_3G, STATE_ON);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_RECEIVE_ON, PHONE_BALANCE, GEN_3G, STATE_ON);
        sendSMS(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE);
    }

    public void testGetLastMadeCommunicationService() {

        // Arrange
        PhoneSimpleDto phoneDto = new PhoneSimpleDto(SMS_NUMBER_SEND_ON);
        GetLastMadeCommunicationService getService = new GetLastMadeCommunicationService(phoneDto);
        CommunicationDto lastCommunication = null;

        // Act
        try {
            getService.execute();
            lastCommunication = getService.getLastMadeCommunicationServiceResult();
        } catch (PhoneException e) {
            fail("Should be executed and should not throw an exception.");
        } catch (OperatorException e) {
            fail("Should be executed and should not throw an exception.");
        } catch (AnacomException e) {
            fail("Should be executed and should not throw an exception.");
        }

        // Assert
        assertTrue("COMMUNICATIONS EQUAL ", checkLastCommunication(SMS_NUMBER_SEND_ON, lastCommunication));
    }

    public void testGetLastMadeCommunicationEmptyService() {
        // Arrange
        PhoneSimpleDto phoneDto = new PhoneSimpleDto(SMS_NUMBER_RECEIVE_ON);
        GetLastMadeCommunicationService getService = new GetLastMadeCommunicationService(phoneDto);

        // Act
        try {
            getService.execute();
            fail("Should throw an exception. The number has not made any communications yet.");
        } catch (PhoneException e) {
        } catch (OperatorException e) {
        } catch (AnacomException e) {
        }
    }
}
