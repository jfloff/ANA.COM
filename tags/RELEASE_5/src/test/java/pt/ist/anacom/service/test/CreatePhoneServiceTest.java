package pt.ist.anacom.service.test;

import pt.ist.anacom.service.RegisterNewPhoneService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.PhoneDetailedDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.OperatorException;
import pt.ist.anacom.shared.exception.PhoneException;

public class CreatePhoneServiceTest extends AnacomServiceTestCase {

	private static String OPERATOR_NAME = "SDES";
	private static String OPERATOR_PREFIX = "98";
	private static String PHONE_NUMBER = "981111111";
	private static String PHONE_NUMBER_WRONGLENGHT = "98111111234211";
	private static String PHONE_NUMBER_WITHOUTOPERATOR = "958888888";
	private static String OPERATOR_NAME_OTHER = "ONE";
	private static String OPERATOR_PREFIX_OTHER = "99";
	private static String PHONE_NUMBER_OTHER = "997777777";
	private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;
	private static AnacomData.State STATE_ON = AnacomData.State.ON;

	public CreatePhoneServiceTest(String msg) {
		super(msg);
	}

	public CreatePhoneServiceTest() {
		super();
	}

	@Override
	public void setUp() {
		super.setUp();
		addOperator(OPERATOR_PREFIX, OPERATOR_NAME, 0);
		addOperator(OPERATOR_PREFIX_OTHER,OPERATOR_NAME_OTHER,0);
		addPhone(OPERATOR_PREFIX_OTHER,PHONE_NUMBER_OTHER,0,GEN_2G,STATE_ON);
	}

	public void testCreatePhone() {

		// Arrange
		PhoneDetailedDto dto = new PhoneDetailedDto(OPERATOR_PREFIX,PHONE_NUMBER,GEN_2G);
		RegisterNewPhoneService createService = new RegisterNewPhoneService(dto);

		// Act
		try {
			createService.execute();
		} catch (PhoneException e) {
			fail("Phone does not exist yet. " + "Should be added without throwing an exception.");
		} catch (OperatorException e) {
			fail("Operator does not exist yet. " + "Should be added without throwing an exception.");
		} catch (AnacomException e) {
			fail("Operator does not exist yet. " + "Should be added without throwing an exception.");
		}

		// Assert
		assertTrue("Phone Exists ", checkPhone(OPERATOR_PREFIX,PHONE_NUMBER));
	}


	public void testCreatePhoneWithWrongLength() {

		// Arrange
		PhoneDetailedDto dto = new PhoneDetailedDto(OPERATOR_PREFIX,PHONE_NUMBER_WRONGLENGHT,GEN_2G);
		RegisterNewPhoneService createService = new RegisterNewPhoneService(dto);

		// Act
		try {
			createService.execute();
			fail("Phone created. " + "Should not be added and throw an exception.");
		} catch (OperatorException e) {

		}catch (AnacomException e) {

		}
	}
	
	public void testCreatePhoneWithoutOperator() {

		// Arrange
		PhoneDetailedDto dto = new PhoneDetailedDto(OPERATOR_PREFIX,PHONE_NUMBER_WITHOUTOPERATOR,GEN_2G);
		RegisterNewPhoneService createService = new RegisterNewPhoneService(dto);

		// Act
		try {
			createService.execute();
			fail("Phone created. " + "Should not be added and throw an exception.");
		} catch (OperatorException e) {

		}catch (AnacomException e) {

		}
	}

	public void testCreateDuplicatePhone() {

		// Arrange
		PhoneDetailedDto dto = new PhoneDetailedDto(OPERATOR_PREFIX_OTHER,PHONE_NUMBER_OTHER,GEN_2G);
		RegisterNewPhoneService createService = new RegisterNewPhoneService(dto);

		// Act
		try {
			createService.execute();
			fail("Phone created." + "Should not be added and throw an exception.");
		} catch (OperatorException e) {

		} catch (AnacomException e) {

		}
	}
}
