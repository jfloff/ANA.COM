package pt.ist.anacom.service.test;

import java.util.ArrayList;

import pt.ist.anacom.service.GetSMSPhoneReceivedListService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.dto.SMSDto;
import pt.ist.anacom.shared.dto.SMSPhoneReceivedListDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.PhoneException;

public class GetPhoneReceivedSMSListServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_NAME = "SDES";
    private static String OPERATOR_NAME_SEC = "TMN";
    private static String OPERATOR_PREFIX = "98";
    private static String OPERATOR_PREFIX_SEC = "96";
    private static String SMS_NUMBER_SEND_ON = "982222222";
    private static String SMS_NUMBER_RECEIVE_ON = "981111111";
    private static String SMS_NUMBER_NOT_EXISTS = "983456789";
    private static String SMS_NUMBER_DIFF_PREFIX = "961111111";
    private static String SMS_MESSAGE = "Hello";
    private static String SMS_MESSAGE_DIFF = "Bye";
    private static String SMS_MESSAGE_END = "End";
    private static int PHONE_BALANCE = 100;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;
    private static AnacomData.PhoneType GEN_3G = AnacomData.PhoneType.GEN3;
    private static AnacomData.State STATE_ON = AnacomData.State.ON;
    private static ArrayList<SMSDto> SMSLIST = new ArrayList<SMSDto>();


    public GetPhoneReceivedSMSListServiceTest(String msg) {
        super(msg);
    }

    public GetPhoneReceivedSMSListServiceTest() {
        super();
    }


    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME, 0);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_SEND_ON, PHONE_BALANCE, GEN_2G, STATE_ON);
        addPhone(OPERATOR_PREFIX, SMS_NUMBER_RECEIVE_ON, PHONE_BALANCE, GEN_3G, STATE_ON);

        sendSMS(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE);
        SMSLIST.add(new SMSDto(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE));

        sendSMS(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE_DIFF);
        SMSLIST.add(new SMSDto(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE_DIFF));

        sendSMS(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE_END);
        SMSLIST.add(new SMSDto(SMS_NUMBER_SEND_ON, SMS_NUMBER_RECEIVE_ON, SMS_MESSAGE_END));

        addOperator(OPERATOR_PREFIX_SEC, OPERATOR_NAME_SEC, 0);
        addPhone(OPERATOR_PREFIX_SEC, SMS_NUMBER_DIFF_PREFIX, PHONE_BALANCE, GEN_3G, STATE_ON);
        sendSMS(SMS_NUMBER_RECEIVE_ON, SMS_NUMBER_DIFF_PREFIX, SMS_MESSAGE_END);
    }

    public void testGetReceivedSMSListPhoneNotExistService() {

        // Arrange
        PhoneSimpleDto phoneDto = new PhoneSimpleDto(SMS_NUMBER_NOT_EXISTS);
        GetSMSPhoneReceivedListService getService = new GetSMSPhoneReceivedListService(phoneDto);

        // Act
        try {
            getService.execute();
            fail("The phone number does not exist therefore should not get sms list. Should throw an exception.");
        } catch (PhoneException e) {
        }
    }

    public void testGetReceivedSMSListDiffOperatorsServiceTest() {

        // Arrange
        PhoneSimpleDto phoneDto = new PhoneSimpleDto(SMS_NUMBER_DIFF_PREFIX);
        GetSMSPhoneReceivedListService getService = new GetSMSPhoneReceivedListService(phoneDto);

        SMSPhoneReceivedListDto resultDto = null;

        // Act
        try {
            getService.execute();
            resultDto = getService.getSMSPhoneReceivedListServiceResult();
        } catch (AnacomException e) {
            fail("SMS Received List does not exist. Should be returned without throwing an exception");
        }

        // Assert
        assertTrue("SMS LISTS EQUAL ", checkReceivedSMSList(SMS_NUMBER_DIFF_PREFIX, resultDto.getSmsList()));
        assertFalse("SMS NOT EMPTY", resultDto.getSmsList().isEmpty());
    }

    public void testGetReceivedSMSListServiceTest() {

        // Arrange
        PhoneSimpleDto phoneDto = new PhoneSimpleDto(SMS_NUMBER_RECEIVE_ON);
        GetSMSPhoneReceivedListService getService = new GetSMSPhoneReceivedListService(phoneDto);

        SMSPhoneReceivedListDto resultDto = null;

        // Act
        try {
            getService.execute();
            resultDto = getService.getSMSPhoneReceivedListServiceResult();
        } catch (AnacomException e) {
            fail("SMS Received List does not exist. Should be returned without throwing an exception");
        }

        // Assert
        assertTrue("SMS LISTS EQUAL ", checkReceivedSMSList(SMS_NUMBER_RECEIVE_ON, resultDto.getSmsList()));
        assertFalse("SMS NOT EMPTY", resultDto.getSmsList().isEmpty());
    }
}
