package pt.ist.anacom.service.test;

import pt.ist.anacom.exception.AnacomException;
import pt.ist.anacom.service.ProcessIncBalanceService;
import pt.ist.anacom.service.dto.BalanceDto;

public class IncreasePhoneBalanceServiceTest extends AnacomServiceTestCase {

	private static String OPERATOR_NAME = "ESSD";
	private static String OPERATOR_PREFIX = "98";
	private static String NUMBER = "988888888";
	private static String NOT_NUMBER = "983456789";
	private static int AMOUNT_TO_INCREASE = 10;
	private static int AMOUNT_EXCEED_TO_INCREASE = 150;
	private static int AMOUNT_NEGATIVE_TO_INCREASE = -10;
	private static int BALANCE = 10;

	public IncreasePhoneBalanceServiceTest() {
		super();
	}

	public IncreasePhoneBalanceServiceTest(String msg) {
		super(msg);
	}

	@Override
	public void setUp() {
		super.setUp();
		addOperator(OPERATOR_PREFIX, OPERATOR_NAME);
		addPhone(OPERATOR_NAME, NUMBER, BALANCE, null);
	}

	public void testIncreasePhoneBalance() {

		// Arrange
		BalanceDto dto = new BalanceDto(NUMBER, AMOUNT_TO_INCREASE);
		ProcessIncBalanceService incService = new ProcessIncBalanceService(dto);
		int currentBalance = getPhoneBalance(NUMBER);

		// Act
		try {
			incService.execute();
		} catch (AnacomException e) {
			fail("Balance does not increase. " + "Should be added without throwing an exception.");
		}

		// Assert
		assertEquals("The balance of phone should be increased by " + AMOUNT_TO_INCREASE + ".", currentBalance + AMOUNT_TO_INCREASE, getPhoneBalance(NUMBER));

	}

	/**
	 * Balance Limit = 100
	 */

	public void testIncreasePhoneBalanceGreaterThanLimit() {

		// Arrange
		BalanceDto dto = new BalanceDto(NUMBER, AMOUNT_EXCEED_TO_INCREASE);
		ProcessIncBalanceService incService = new ProcessIncBalanceService(dto);
		int currentBalance = getPhoneBalance(NUMBER);

		// Act
		try {
			incService.execute();
			fail("Balance does increase. Should throw an exception.");
		} catch (AnacomException e) {

			System.err.println(e.getMessage());
			// Assert
			assertEquals("The balance of phone should not be increased by " + AMOUNT_EXCEED_TO_INCREASE + ". Balance should stay the same ", currentBalance,
					getPhoneBalance(NUMBER));
		}

	}

	public void testIncreaseNegativeAmountPhoneBalance() {

		// Arrange
		BalanceDto dto = new BalanceDto(NUMBER, AMOUNT_NEGATIVE_TO_INCREASE);
		ProcessIncBalanceService incService = new ProcessIncBalanceService(dto);
		int currentBalance = getPhoneBalance(NUMBER);

		// Act
		try {
			incService.execute();
			fail("Balance does increase. Should throw an exception.");
		} catch (AnacomException e) {

			System.err.println(e.getMessage());
			// Assert
			assertEquals("The balance of phone should not be increased by " + AMOUNT_NEGATIVE_TO_INCREASE + ". Balance should stay the same ", currentBalance,
					getPhoneBalance(NUMBER));
		}

	}

	public void testIncreasePhoneInexistentBalance() {

		// Arrange
		BalanceDto dto = new BalanceDto(NOT_NUMBER, AMOUNT_TO_INCREASE);
		ProcessIncBalanceService incService = new ProcessIncBalanceService(dto);

		// Act
		try {
			incService.execute();
			fail("Balance did increase. " + "Should not be increased and throw an exception");
		} catch (AnacomException e) {
			System.err.println(e.getMessage());
		}

	}

}
