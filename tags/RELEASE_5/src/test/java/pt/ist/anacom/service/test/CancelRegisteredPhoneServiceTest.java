package pt.ist.anacom.service.test;

import pt.ist.anacom.service.CancelRegisteredPhoneService;
import pt.ist.anacom.service.RegisterNewPhoneService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.OperatorException;
import pt.ist.anacom.shared.exception.PhoneException;

public class CancelRegisteredPhoneServiceTest extends AnacomServiceTestCase {

	private static String OPERATOR_NAME = "SDES";
	private static String OPERATOR_PREFIX = "98";
	private static String OPERATOR_PREFIX_OTHER = "91";
	private static String PHONE_NUMBER = "981111111";
	private static String PHONE_NUMBER_WITHOUTOPERATOR = "982222222";
	private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;
	private static AnacomData.State STATE_ON = AnacomData.State.ON;

	public CancelRegisteredPhoneServiceTest(String msg) {
		super(msg);
	}

	public CancelRegisteredPhoneServiceTest() {
		super();
	}

	@Override
	public void setUp() {
		super.setUp();
		addOperator(OPERATOR_PREFIX, OPERATOR_NAME, 0);
		addPhone(OPERATOR_PREFIX,PHONE_NUMBER,0,GEN_2G,STATE_ON);
	}

	public void testPhoneOperator() {

		// Arrange
		PhoneDetailedDto dto = new PhoneDetailedDto(OPERATOR_PREFIX,PHONE_NUMBER,GEN_2G);
		CancelRegisteredPhoneService createService = new CancelRegisteredPhoneService(dto);

		// Act
		try {
			createService.execute();
		} catch (PhoneException e) {
			fail("Phone still exists. " + "Should be removed without throwing an exception.");
		} catch (OperatorException e) {
			fail("Operator still exists. " + "Should be removed without throwing an exception.");
		} catch (AnacomException e) {
			fail("Operator still exists. " + "Should be removed without throwing an exception.");
		}

		// Assert
		assertTrue("Phone not exist ", checkCancelPhone());
	}

	public void testCancelNotExistedPhone() {

		// Arrange
		PhoneDetailedDto dto = new PhoneDetailedDto(OPERATOR_PREFIX,PHONE_NUMBER_WITHOUTOPERATOR,GEN_2G);
		CancelRegisteredPhoneService createService = new CancelRegisteredPhoneService(dto);

		// Act
		try {
			createService.execute();
			fail("Phone canceled. " + "Should not be canceled and throw an exception.");
		}catch (AnacomException e) {

		}
	}
}
