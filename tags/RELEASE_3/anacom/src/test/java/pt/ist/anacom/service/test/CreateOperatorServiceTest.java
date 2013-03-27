package pt.ist.anacom.service.test;

import pt.ist.anacom.service.ProcessRegisterNewOperatorService;
import pt.ist.anacom.shared.dto.OperatorDto;
import pt.ist.anacom.shared.exception.AnacomException;

public class CreateOperatorServiceTest extends AnacomServiceTestCase {

	private static String OPERATOR_NAME = "SDES";
	private static String OPERATOR_PREFIX = "98";
	private static String OPERATOR_WRONG_PREFIX = "922";
	private static int TAX = 2;
	private static int VOICETAX = 3;
	private static int SMSTAX = 1;
	private static int VIDEOTAX = 5;

	private static String OPERATOR_NAME_OTHER = "ONE";
	private static String OPERATOR_PREFIX_OTHER = "99";

	public CreateOperatorServiceTest(String msg) {
		super(msg);
	}

	public CreateOperatorServiceTest() {
		super();
	}

	@Override
	public void setUp() {
		super.setUp();
		addOperator(OPERATOR_PREFIX_OTHER, OPERATOR_NAME_OTHER);
	}

	public void testCreateOperator() {

		// Arrange
		OperatorDto dto = new OperatorDto(OPERATOR_PREFIX, OPERATOR_NAME, TAX, VOICETAX, SMSTAX, VIDEOTAX);
		ProcessRegisterNewOperatorService createService = new ProcessRegisterNewOperatorService(dto);

		// Act
		try {
			createService.execute();

		} catch (AnacomException e) {
			fail("Operator does not exist yet. " + "Should be added without throwing an exception.");
		}

		// Assert
		assertTrue("Operator Exists ", checkOperator(OPERATOR_PREFIX));
	}

	public void testCreateOperatorWithWrongPrefix() {

		// Arrange
		OperatorDto dto = new OperatorDto(OPERATOR_WRONG_PREFIX, OPERATOR_NAME, TAX, VOICETAX, SMSTAX, VIDEOTAX);
		ProcessRegisterNewOperatorService createService = new ProcessRegisterNewOperatorService(dto);

		// Act
		try {
			createService.execute();
			fail("Operator created. " + "Should not be added and throw an exception.");

		} catch (AnacomException e) {

			System.err.println(e.getMessage());
			// Assert
			assertFalse("Operator doesn't exists ", checkOperator(OPERATOR_NAME));
		}

	}

	public void testCreateOperatorWithoutName() {

		// Arrange
		OperatorDto dto = new OperatorDto(OPERATOR_PREFIX, null, TAX, VOICETAX, SMSTAX, VIDEOTAX);
		ProcessRegisterNewOperatorService createService = new ProcessRegisterNewOperatorService(dto);

		// Act
		try {
			createService.execute();
			fail("Operator created. " + "Should not be added and throw an exception.");

		} catch (AnacomException e) {

			System.err.println(e.getMessage());
			// Assert
			assertFalse("Operator doesn't exists ", checkOperator(null));
		}

	}

	public void testCreateDuplicateOperator() {

		// Arrange
		OperatorDto dto = new OperatorDto(OPERATOR_PREFIX_OTHER, OPERATOR_NAME_OTHER, TAX, VOICETAX, SMSTAX, VIDEOTAX);
		ProcessRegisterNewOperatorService createService = new ProcessRegisterNewOperatorService(dto);

		// Act
		try {
			createService.execute();
			fail("Operator created." + "Should not be added and throw an exception.");
		} catch (AnacomException e) {

		}
	}
}
