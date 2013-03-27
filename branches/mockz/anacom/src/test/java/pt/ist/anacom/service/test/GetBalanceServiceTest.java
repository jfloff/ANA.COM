package pt.ist.anacom.service.test;

import pt.ist.anacom.service.GetBalanceService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.PhoneException;

public class GetBalanceServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_NAME = "ESSD";
    private static String OPERATOR_PREFIX = "98";
    private static int BONUS = 2;
    private static String NUMBER = "988888888";
    private static String NUMBER_INEXISTENT = "911111111";
    private static int BALANCE = 1000;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;

    public GetBalanceServiceTest() {
        super();
    }

    public GetBalanceServiceTest(String msg) {
        super(msg);
    }

    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME, BONUS);
        addPhone(OPERATOR_PREFIX, NUMBER, BALANCE, GEN_2G, null);
    }

    public void testGetPhoneBalance() {

        // Arrange
        PhoneSimpleDto dto = new PhoneSimpleDto(NUMBER);
        GetBalanceService service = new GetBalanceService(dto);

        // Act
        try {
            service.execute();
        } catch (PhoneException e) {
            fail("Balance did not return. Should be returned without throwing an exception.");
        } catch (AnacomException e) {
            fail("Balance did not return. Should be returned without throwing an exception.");
        }

        // Assert
        assertEquals("The phone balance should be " + BALANCE + ".", service.getBalanceServiceResult().getBalance(), getPhoneBalance(NUMBER));
    }

    public void testGetPhoneBalanceInexistentPhone() {

        // Arrange
        PhoneSimpleDto dto = new PhoneSimpleDto(NUMBER_INEXISTENT);
        GetBalanceService service = new GetBalanceService(dto);

        // Act
        try {
            service.execute();
            fail("Phone does not exist therefore balance should not be returned. Should throw an exception");
        } catch (PhoneException e) {
        } catch (AnacomException e) {
        }
    }
}
