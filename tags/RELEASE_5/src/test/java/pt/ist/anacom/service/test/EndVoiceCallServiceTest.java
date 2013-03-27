package pt.ist.anacom.service.test;

import pt.ist.anacom.service.ProcessEndDestinationVoiceCallService;
import pt.ist.anacom.service.ProcessEndSourceVoiceCallService;
import pt.ist.anacom.service.ProcessStartDestinationVoiceCallService;
import pt.ist.anacom.service.ProcessStartSourceVoiceCallService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.dto.CommunicationDurationDto;
import pt.ist.anacom.shared.exception.AnacomException;

public class EndVoiceCallServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_NAME = "SDES";
    private static String OPERATOR_PREFIX = "98";
    private static String CALLER_PHONE_NUMBER_ON = "981111111";
    private static String RECEIVER_PHONE_NUMBER_ON = "982222222";
    private static String CALLER_PHONE_NUMBER_SILENCE = "983333333";
    private static String RECEIVER_PHONE_NUMBER_ON2 = "984444444";
    private static int CALL_DURATION = 10;
    private static int PHONE_INITIAL_BALANCE = 3000;
    private static int PHONE_FAKE_BALANCE = 1000;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;
    private static AnacomData.PhoneType GEN_3G = AnacomData.PhoneType.GEN3;
    private static AnacomData.State STATE_ON = AnacomData.State.ON;
    private static AnacomData.State STATE_BUSY = AnacomData.State.BUSY;
    private static AnacomData.State STATE_SILENCE = AnacomData.State.SILENCE;
    
    public EndVoiceCallServiceTest(String msg) {
        super(msg);
    }

    public EndVoiceCallServiceTest() {
        super();
    }

    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME, 0);
        
        addPhone(OPERATOR_PREFIX, CALLER_PHONE_NUMBER_ON, PHONE_INITIAL_BALANCE, GEN_2G, STATE_ON);
        addPhone(OPERATOR_PREFIX, RECEIVER_PHONE_NUMBER_ON, PHONE_INITIAL_BALANCE, GEN_3G, STATE_ON);
        startVoiceCall(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_ON);
        
        addPhone(OPERATOR_PREFIX, CALLER_PHONE_NUMBER_SILENCE, PHONE_INITIAL_BALANCE, GEN_3G, STATE_SILENCE);
        addPhone(OPERATOR_PREFIX, RECEIVER_PHONE_NUMBER_ON2, PHONE_INITIAL_BALANCE, GEN_3G, STATE_ON);

        startVoiceCall(CALLER_PHONE_NUMBER_SILENCE, RECEIVER_PHONE_NUMBER_ON2);

    }

    public void testEndVoiceCallServiceTest() {

        // Arrange
        CommunicationDurationDto dto = new CommunicationDurationDto(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_ON, CALL_DURATION);
        ProcessEndSourceVoiceCallService endSourceService = new ProcessEndSourceVoiceCallService(dto);
        ProcessEndDestinationVoiceCallService endDestinationService = new ProcessEndDestinationVoiceCallService(dto);
        
        
        // Act
        try {
        	endSourceService.execute();
        	endDestinationService.execute();
        } catch (AnacomException e) {
        	fail("Communication was not made. " + "Should be sent without throwing an exception.");
        }
        
        assertTrue(!checkExistsCommunication(CALLER_PHONE_NUMBER_ON));
        assertTrue(!checkExistsCommunication(RECEIVER_PHONE_NUMBER_ON));
    }
    
    public void testEndVoiceCallCorrectFinalBalanceServiceTest() {

        // Arrange
        CommunicationDurationDto dto = new CommunicationDurationDto(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_ON, CALL_DURATION);
        ProcessEndSourceVoiceCallService endSourceService = new ProcessEndSourceVoiceCallService(dto);
        ProcessEndDestinationVoiceCallService endDestinationService = new ProcessEndDestinationVoiceCallService(dto);
        
        
        // Act
        try {
        	endSourceService.execute();
        	endDestinationService.execute();
        } catch (AnacomException e) {
        	fail("Communication was not made. " + "Should be sent without throwing an exception.");
        }
        
        assertTrue(checkCorrectFinalBalance(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_ON, CALL_DURATION, PHONE_INITIAL_BALANCE));
        assertFalse(checkCorrectFinalBalance(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_ON, CALL_DURATION, PHONE_FAKE_BALANCE));
    }

    
    public void testEndVoiceCallCorrectPreviousStateServiceTest() {

        // Arrange
        CommunicationDurationDto dto = new CommunicationDurationDto(CALLER_PHONE_NUMBER_SILENCE, RECEIVER_PHONE_NUMBER_ON2, CALL_DURATION);
        ProcessEndSourceVoiceCallService endSourceService = new ProcessEndSourceVoiceCallService(dto);
        ProcessEndDestinationVoiceCallService endDestinationService = new ProcessEndDestinationVoiceCallService(dto);
        
        
        // Act
        try {
        	endSourceService.execute();
        	endDestinationService.execute();
        } catch (AnacomException e) {
        	fail("Communication was not made. " + "Should be sent without throwing an exception.");
        }
        
        assertTrue(checkPreviousState(CALLER_PHONE_NUMBER_SILENCE, STATE_SILENCE));
        assertTrue(!checkPreviousState(CALLER_PHONE_NUMBER_SILENCE, STATE_BUSY));
    }
    
}
