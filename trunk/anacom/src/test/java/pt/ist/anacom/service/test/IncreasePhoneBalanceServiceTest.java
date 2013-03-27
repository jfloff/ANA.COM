package pt.ist.anacom.service.test;

import pt.ist.anacom.service.IncreaseBalanceService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.BalanceAndPhoneDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.PhoneException;

public class IncreasePhoneBalanceServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_NAME = "ESSD";
    private static String OPERATOR_PREFIX = "98";
    private static int BONUS = 2;
    private static String NUMBER = "988888888";
    private static String NOT_NUMBER = "983456789";
    private static int AMOUNT_TO_INCREASE = 100;
    private static int AMOUNT_TO_EXCEED_BONUS = 8990;
    private static int MAX_BALANCE = 10000;
    private static int AMOUNT_EXCEED_TO_INCREASE = 15000;
    private static int AMOUNT_NEGATIVE_TO_INCREASE = -10;
    private static int BALANCE = 1000;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;

    public IncreasePhoneBalanceServiceTest() {
        super();
    }

    public IncreasePhoneBalanceServiceTest(String msg) {
        super(msg);
    }

    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME, BONUS);
        addPhone(OPERATOR_PREFIX, NUMBER, BALANCE, GEN_2G, null);
    }

    public void testIncreasePhoneBalance() {

        // Arrange
        BalanceAndPhoneDto dto = new BalanceAndPhoneDto(NUMBER, AMOUNT_TO_INCREASE);
        IncreaseBalanceService incService = new IncreaseBalanceService(dto);
        int currentBalance = getPhoneBalance(NUMBER);
        int finalAmount = AMOUNT_TO_INCREASE * (101 + BONUS) / 100;
        // Act
        try {
            incService.execute();
        } catch (PhoneException e) {
            fail("Balance does not increase. " + "Should be added without throwing an exception.");
        } catch (AnacomException e) {
            fail("Balance does not increase. " + "Should be added without throwing an exception.");
        }

        // Assert
        assertEquals("The balance of phone should be increased by " + AMOUNT_TO_INCREASE + ".", currentBalance + finalAmount, getPhoneBalance(NUMBER));

    }


    public void testIncreasePhoneBalanceBonusExceed() {

        // Arrange
        BalanceAndPhoneDto dto = new BalanceAndPhoneDto(NUMBER, AMOUNT_TO_EXCEED_BONUS);
        IncreaseBalanceService incService = new IncreaseBalanceService(dto);

        // Act
        try {
            incService.execute();
        } catch (PhoneException e) {
            fail("Balance does not increase. " + "Should be added without throwing an exception.");
        } catch (AnacomException e) {
            fail("Balance does not increase. " + "Should be added without throwing an exception.");
        }

        // Assert
        assertEquals("The balance of phone should be " + MAX_BALANCE + ".", MAX_BALANCE, getPhoneBalance(NUMBER));

    }

    /**
     * Balance Limit = 10000
     */

    public void testIncreasePhoneBalanceGreaterThanLimit() {

        // Arrange
        BalanceAndPhoneDto dto = new BalanceAndPhoneDto(NUMBER, AMOUNT_EXCEED_TO_INCREASE);
        IncreaseBalanceService incService = new IncreaseBalanceService(dto);
        int currentBalance = getPhoneBalance(NUMBER);

        // Act
        try {
            incService.execute();
            fail("Balance does increase. Should throw an exception.");
        } catch (PhoneException e) {

            // Assert
            assertEquals("The balance of phone should not be increased by " + AMOUNT_EXCEED_TO_INCREASE + ". Balance should stay the same ",
                         currentBalance,
                         getPhoneBalance(NUMBER));
        }

    }

    public void testIncreaseNegativeAmountPhoneBalance() {

        // Arrange
        BalanceAndPhoneDto dto = new BalanceAndPhoneDto(NUMBER, AMOUNT_NEGATIVE_TO_INCREASE);
        IncreaseBalanceService incService = new IncreaseBalanceService(dto);
        int currentBalance = getPhoneBalance(NUMBER);

        // Act
        try {
            incService.execute();
            fail("Balance does increase. Should throw an exception.");
        } catch (PhoneException e) {

            // Assert
            assertEquals("The balance of phone should not be increased by " + AMOUNT_NEGATIVE_TO_INCREASE + ". Balance should stay the same ",
                         currentBalance,
                         getPhoneBalance(NUMBER));
        }

    }

    public void testIncreasePhoneInexistentBalance() {

        // Arrange
        BalanceAndPhoneDto dto = new BalanceAndPhoneDto(NOT_NUMBER, AMOUNT_TO_INCREASE);
        IncreaseBalanceService incService = new IncreaseBalanceService(dto);

        // Act
        try {
            incService.execute();
            fail("Balance did increase. " + "Should not be increased and throw an exception");
        } catch (PhoneException e) {
        } catch (AnacomException e) {
        }
    }

}
