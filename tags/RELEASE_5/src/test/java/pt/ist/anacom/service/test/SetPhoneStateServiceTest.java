package pt.ist.anacom.service.test;

import pt.ist.anacom.service.SetPhoneStateService;
import pt.ist.anacom.shared.data.AnacomData;
import pt.ist.anacom.shared.dto.PhoneAndStateDto;
import pt.ist.anacom.shared.exception.AnacomException;
import pt.ist.anacom.shared.exception.PhoneException;

public class SetPhoneStateServiceTest extends AnacomServiceTestCase {

    private static String OPERATOR_NAME = "ESSD";
    private static String OPERATOR_PREFIX = "98";
    private static int BONUS = 2;
    private static String NUMBER = "988888888";
    private static String NUMBER_INEXISTENT = "911111111";
    private static AnacomData.State STATE_ON = AnacomData.State.ON;
    private static AnacomData.State STATE_OFF = AnacomData.State.OFF;
    private static AnacomData.State STATE_BUSY = AnacomData.State.BUSY;
    private static AnacomData.State STATE_SILENCE = AnacomData.State.SILENCE;
    private static int BALANCE = 1000;
    private static AnacomData.PhoneType GEN_2G = AnacomData.PhoneType.GEN2;

    public SetPhoneStateServiceTest() {
        super();
    }

    public SetPhoneStateServiceTest(String msg) {
        super(msg);
    }

    @Override
    public void setUp() {
        super.setUp();
        addOperator(OPERATOR_PREFIX, OPERATOR_NAME, BONUS);
        addPhone(OPERATOR_PREFIX, NUMBER, BALANCE, GEN_2G, null);
    }

    public void testSetPhoneStateON() {

        // Arrange
        PhoneAndStateDto dto = new PhoneAndStateDto(NUMBER, STATE_ON);
        SetPhoneStateService service = new SetPhoneStateService(dto);

        // Act
        try {
            service.execute();
        } catch (PhoneException e) {
            fail("State did not change. Should be changed without throwing an exception.");
        } catch (AnacomException e) {
            fail("State did not change. Should be changed without throwing an exception.");
        }

        // Assert
        assertEquals("The phone state should be " + STATE_ON.name() + ".", STATE_ON, getPhoneState(NUMBER));

    }

    public void testSetPhoneStateOFF() {

        // Arrange
        PhoneAndStateDto dto = new PhoneAndStateDto(NUMBER, STATE_OFF);
        SetPhoneStateService service = new SetPhoneStateService(dto);

        // Act
        try {
            service.execute();
        } catch (PhoneException e) {
            fail("State did not change. Should be changed without throwing an exception.");
        } catch (AnacomException e) {
            fail("State did not change. Should be changed without throwing an exception.");
        }

        // Assert
        assertEquals("The phone state should be " + STATE_OFF.name() + ".", STATE_OFF, getPhoneState(NUMBER));

    }

    public void testSetPhoneStateBUSY() {

        // Arrange
        PhoneAndStateDto dto = new PhoneAndStateDto(NUMBER, STATE_BUSY);
        SetPhoneStateService service = new SetPhoneStateService(dto);

        // Act
        try {
            service.execute();
        } catch (PhoneException e) {
            fail("State did not change. Should be changed without throwing an exception.");
        } catch (AnacomException e) {
            fail("State did not change. Should be changed without throwing an exception.");
        }

        // Assert
        assertEquals("The phone state should be " + STATE_BUSY.name() + ".", STATE_BUSY, getPhoneState(NUMBER));


    }

    public void testSetPhoneStateSILENCE() {

        // Arrange
        PhoneAndStateDto dto = new PhoneAndStateDto(NUMBER, STATE_SILENCE);
        SetPhoneStateService service = new SetPhoneStateService(dto);

        // Act
        try {
            service.execute();
        } catch (PhoneException e) {
            fail("State did not change. Should be changed without throwing an exception.");
        } catch (AnacomException e) {
            fail("State did not change. Should be changed without throwing an exception.");
        }

        // Assert
        assertEquals("The phone state should be " + STATE_SILENCE.name() + ".", STATE_SILENCE, getPhoneState(NUMBER));
    }

    public void testSetPhoneStateInexistentPhone() {

        // Arrange
        PhoneAndStateDto dto = new PhoneAndStateDto(NUMBER_INEXISTENT, STATE_SILENCE);
        SetPhoneStateService service = new SetPhoneStateService(dto);

        // Act
        try {
            service.execute();
            fail("Phone does not exist therefore state should not be changed. Should throw an exception");
        } catch (PhoneException e) {
        } catch (AnacomException e) {
        }
    }
}
