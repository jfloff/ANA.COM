package pt.ist.anacom.service.test;

import pt.ist.anacom.service.GetPhoneStateService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.PhoneException;

public class GetPhoneStateServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_NAME = "ESSD";
    private static String OPERATOR_PREFIX = "98";
    private static int BONUS = 2;
    private static String NUMBER = "988888888";
    private static String NUMBER_INEXISTENT = "911111111";
    private static AnacomData.State STATE_ON = AnacomData.State.ON;
    private static int BALANCE = 1000;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;

    public GetPhoneStateServiceTest() {
        super();
    }

    public GetPhoneStateServiceTest(String msg) {
        super(msg);
    }

    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME, BONUS);
        addPhone(OPERATOR_PREFIX, NUMBER, BALANCE, GEN_2G, null);
    }

    public void testGetPhoneState() {

        // Arrange
        PhoneSimpleDto dto = new PhoneSimpleDto(NUMBER);
        GetPhoneStateService service = new GetPhoneStateService(dto);

        // Act
        try {
            service.execute();
        } catch (PhoneException e) {
            fail("State did not return. Should be returned without throwing an exception.");
        } catch (AnacomException e) {
            fail("State did not return. Should be returned without throwing an exception.");
        }

        // Assert
        assertEquals("The phone state should be " + STATE_ON.name() + ".", service.getPhoneStateServiceResult().getState(), getPhoneState(NUMBER));
    }

    public void testGetPhoneStateInexistentPhone() {

        // Arrange
        PhoneSimpleDto dto = new PhoneSimpleDto(NUMBER_INEXISTENT);
        GetPhoneStateService service = new GetPhoneStateService(dto);

        // Act
        try {
            service.execute();
            fail("Phone does not exist therefore state should not be returned. Should throw an exception");
        } catch (PhoneException e) {
        } catch (AnacomException e) {
        }
    }
}
