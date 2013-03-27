package pt.ist.anacom.service.test;

import java.util.ArrayList;

import pt.ist.anacom.service.GetBalanceAndPhoneListService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.dto.BalanceAndPhoneListDto;
import pt.ist.anacom.shared.dto.OperatorSimpleDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.OperatorException;

public class GetBalanceAndPhoneListServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_NAME = "SDES";
    private static String OPERATOR_PREFIX = "98";
    private static String OPERATOR_PREFIX_EMPTY = "96";
    private static String OPERATOR_NAME_EMPTY = "TMN";
    private static String OPERATOR_NOT_EXISTS = "91";
    private static String PHONE_NUMBER_INC100 = "982222222";
    private static int BALANCE100 = 100;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;
    private static AnacomData.State STATE_ON = AnacomData.State.ON;
    private static ArrayList<BalanceAndPhoneDto> BALANCEANDPHONELIST = new ArrayList<BalanceAndPhoneDto>();


    public GetBalanceAndPhoneListServiceTest(String msg) {
        super(msg);
    }

    public GetBalanceAndPhoneListServiceTest() {
        super();
    }


    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME, 0);
        addPhone(OPERATOR_PREFIX, PHONE_NUMBER_INC100, BALANCE100, GEN_2G, STATE_ON);
        BALANCEANDPHONELIST.add(new BalanceAndPhoneDto(PHONE_NUMBER_INC100, BALANCE100));
        
        addOperator(OPERATOR_PREFIX_EMPTY, OPERATOR_NAME_EMPTY, 0);
        
    }

    public void testGetBalanceAndPhoneListOperatorNotExistService() {

        // Arrange
        OperatorSimpleDto operatorDto = new OperatorSimpleDto(OPERATOR_NOT_EXISTS);
        GetBalanceAndPhoneListService getService = new GetBalanceAndPhoneListService(operatorDto);

        // Act
        try {
            getService.execute();
            fail("The operator prefix does not exist therefore should not get Balance and Phone list. Should throw an exception.");
        } catch (OperatorException e) {
        }
    }

    public void testGetBalanceAndPhoneListEmptyServiceTest() {

        // Arrange
    	OperatorSimpleDto operatorDto = new OperatorSimpleDto(OPERATOR_PREFIX_EMPTY);
        GetBalanceAndPhoneListService getService = new GetBalanceAndPhoneListService(operatorDto);

        BalanceAndPhoneListDto resultDto = null;

        // Act
        try {
            getService.execute();
            resultDto = getService.getBalanceAndPhoneListServiceResult();
        } catch (AnacomException e) {
            fail("Balance and Phone list does not exist. Should be returned without throwing an exception");
        }

        // Assert
        assertTrue("BALANCE AND PHONE LIST IS EMPTY", resultDto.getPhoneList().isEmpty());
    }
    
    public void testGetBalanceAndPhoneListServiceTest() {

        // Arrange
    	OperatorSimpleDto operatorDto = new OperatorSimpleDto(OPERATOR_PREFIX);
        GetBalanceAndPhoneListService getService = new GetBalanceAndPhoneListService(operatorDto);

        BalanceAndPhoneListDto resultDto = null;

        // Act
        try {
            getService.execute();
            resultDto = getService.getBalanceAndPhoneListServiceResult();
        } catch (AnacomException e) {
            fail("Balance and Phone list does not exist. Should be returned without throwing an exception");
        }
        

        // Assert
        assertTrue("BALANCE AND PHONE LISTS EQUAL ", checkBalanceAndPhoneList(OPERATOR_PREFIX, resultDto.getPhoneList()));
        assertFalse("SMS NOT EMPTY", resultDto.getPhoneList().isEmpty());
    }
}
