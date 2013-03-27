package pt.ist.anacom.service.test;

import pt.ist.anacom.service.ProcessStartDestinationVoiceCallService;
import pt.ist.anacom.service.ProcessStartSourceVoiceCallService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.CommunicationDto;
import pt.ist.anacom.shared.exception.AnacomException;

public class StartVoiceCallServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_NAME = "SDES";
    private static String OPERATOR_PREFIX = "98";
    private static String CALLER_PHONE_NUMBER_ON = "981111111";
    private static String RECEIVER_PHONE_NUMBER_ON = "982222222";
    private static String CALLER_PHONE_NUMBER_OFF = "983333333";
    private static String RECEIVER_PHONE_NUMBER_OFF = "984444444";
    private static String CALLER_PHONE_NUMBER_INSUFFICIENT_BALANCE = "985555555";
    private static String RECEIVER_PHONE_NUMBER_BUSY = "986666666";
    private static String CALLER_PHONE_NUMBER_SILENCE = "987777777";
    private static int PHONE_BALANCE = 3000;
    private static int INUSFFICIENT_PHONE_BALANCE = 2;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;
    private static AnacomData.PhoneType GEN_3G = AnacomData.PhoneType.GEN3;
    private static AnacomData.State STATE_ON = AnacomData.State.ON;
    private static AnacomData.State STATE_OFF = AnacomData.State.OFF;
    private static AnacomData.State STATE_SILENCE = AnacomData.State.SILENCE;

    public StartVoiceCallServiceTest(String msg) {
        super(msg);
    }

    public StartVoiceCallServiceTest() {
        super();
    }

    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME, 0);
        addPhone(OPERATOR_PREFIX, CALLER_PHONE_NUMBER_ON, PHONE_BALANCE, GEN_3G, STATE_ON);
        addPhone(OPERATOR_PREFIX, RECEIVER_PHONE_NUMBER_ON, PHONE_BALANCE, GEN_2G, STATE_ON);
        
        addPhone(OPERATOR_PREFIX, CALLER_PHONE_NUMBER_OFF, PHONE_BALANCE, GEN_3G, STATE_OFF);
        addPhone(OPERATOR_PREFIX, RECEIVER_PHONE_NUMBER_OFF, PHONE_BALANCE, GEN_3G, STATE_OFF);
        
        addPhone(OPERATOR_PREFIX, CALLER_PHONE_NUMBER_INSUFFICIENT_BALANCE, INUSFFICIENT_PHONE_BALANCE, GEN_2G, STATE_SILENCE);

        
        addPhone(OPERATOR_PREFIX, CALLER_PHONE_NUMBER_SILENCE, PHONE_BALANCE, GEN_2G, STATE_SILENCE);
    }

    public void testStartVoiceCallServiceTest() {

        // Arrange
        CommunicationDto dto = new CommunicationDto(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_ON);
        ProcessStartSourceVoiceCallService startSourceService = new ProcessStartSourceVoiceCallService(dto);
        ProcessStartDestinationVoiceCallService startDestinationService = new ProcessStartDestinationVoiceCallService(dto);

        // Act
        try {
        	startSourceService.execute();
        	startDestinationService.execute();
        } catch (AnacomException e) {
        	fail("Communication was not made. " + "Should be sent without throwing an exception.");
        }
        
        assertTrue(checkExistsCommunication(CALLER_PHONE_NUMBER_ON));
        assertTrue(checkExistsCommunication(RECEIVER_PHONE_NUMBER_ON));
        assertFalse(checkExistsCommunication(CALLER_PHONE_NUMBER_OFF));
    }

    public void testStartVoiceCallDestinationOffServiceTest() {

    	 // Arrange
        CommunicationDto dto = new CommunicationDto(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_OFF);
        ProcessStartSourceVoiceCallService startSourceService = new ProcessStartSourceVoiceCallService(dto);
        ProcessStartDestinationVoiceCallService startDestinationService = new ProcessStartDestinationVoiceCallService(dto);

        // Act
        try {
        	startSourceService.execute();
        	startDestinationService.execute();
            fail("Communication was made. Should throw an exception.");
        } catch (AnacomException e) {
        	
        }
        
    }
    
    public void testStartVoiceCallDestinationBusyServiceTest() {

   	 // Arrange
       CommunicationDto dto = new CommunicationDto(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_BUSY);
       ProcessStartSourceVoiceCallService startSourceService = new ProcessStartSourceVoiceCallService(dto);
       ProcessStartDestinationVoiceCallService startDestinationService = new ProcessStartDestinationVoiceCallService(dto);

       // Act
       try {
       	startSourceService.execute();
       	startDestinationService.execute();
           fail("Communication was made. Should throw an exception.");
       } catch (AnacomException e) {
       	
       }
       
   }
    
    public void testStartVoiceCallCheckBothBusyServiceTest() {

        // Arrange
        CommunicationDto dto = new CommunicationDto(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_ON);
        ProcessStartSourceVoiceCallService startSourceService = new ProcessStartSourceVoiceCallService(dto);
        ProcessStartDestinationVoiceCallService startDestinationService = new ProcessStartDestinationVoiceCallService(dto);

        // Act
        try {
        	startSourceService.execute();
        	startDestinationService.execute();
        } catch (AnacomException e) {
        	fail("Communication was not made. " + "Should be sent without throwing an exception.");
        }
        
        assertTrue(checkPhoneBusyState(CALLER_PHONE_NUMBER_ON));
        assertTrue(checkPhoneBusyState(RECEIVER_PHONE_NUMBER_ON));

    }
    
    
    public void testStartVoiceCallSourceOffServiceTest() {

    	 // Arrange
        CommunicationDto dto = new CommunicationDto(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_OFF);
        ProcessStartSourceVoiceCallService startSourceService = new ProcessStartSourceVoiceCallService(dto);
        ProcessStartDestinationVoiceCallService startDestinationService = new ProcessStartDestinationVoiceCallService(dto);

        // Act
        try {
        	startSourceService.execute();
        	startDestinationService.execute();
            fail("Communication was made. Should throw an exception.");
        } catch (AnacomException e) {
        	
        }
          
      }
    
    public void testStartVoiceCallInsufficientBalanceServiceTest() {

   	 // Arrange
       CommunicationDto dto = new CommunicationDto(CALLER_PHONE_NUMBER_ON, RECEIVER_PHONE_NUMBER_OFF);
       ProcessStartSourceVoiceCallService startSourceService = new ProcessStartSourceVoiceCallService(dto);
       ProcessStartDestinationVoiceCallService startDestinationService = new ProcessStartDestinationVoiceCallService(dto);

       // Act
       try {
       	startSourceService.execute();
       	startDestinationService.execute();
           fail("Communication was made. Should throw an exception.");
       } catch (AnacomException e) {
       	
       }
         
     }
    
    public void testStartVoiceCallSourceSilenceServiceTest() {

        // Arrange
        CommunicationDto dto = new CommunicationDto(CALLER_PHONE_NUMBER_SILENCE, RECEIVER_PHONE_NUMBER_ON);
        ProcessStartSourceVoiceCallService startSourceService = new ProcessStartSourceVoiceCallService(dto);
        ProcessStartDestinationVoiceCallService startDestinationService = new ProcessStartDestinationVoiceCallService(dto);

        // Act
        try {
        	startSourceService.execute();
        	startDestinationService.execute();
        } catch (AnacomException e) {
        	fail("Communication was not made. " + "Should be sent without throwing an exception.");
        }
        
        assertTrue(checkExistsCommunication(CALLER_PHONE_NUMBER_SILENCE));
        assertTrue(checkExistsCommunication(RECEIVER_PHONE_NUMBER_ON));
    }
    

}
