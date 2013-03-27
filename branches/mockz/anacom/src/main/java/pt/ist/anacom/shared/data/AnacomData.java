package pt.ist.anacom.shared.data;

public class AnacomData {

    /*
     * ------------------- Data Values -------------------
     */

    public static final int PREFIX_POS = 0;
    public static final int PREFIX_LENGTH = 2;
    public static final int NUMBER_LENGTH = 9;
    public static final int MAX_BALANCE = 10000;
    public static boolean genKeys = false;

    /*
     * ------------------- Names -------------------
     */

    public static final String ORG_NAME = "anacom";

    /*
     * ------------------- Enums -------------------
     */

    public static enum State {
        ON, OFF, BUSY, BUSY_ON, BUSY_SILENCE, SILENCE;
    }

    public static enum PhoneType {
        GEN2, GEN3;
    }

    public static enum CommunicationType {
        SMS, VOICE, VIDEO;
    }

    public static int convertStateEnumToInt(State state) {

        switch (state) {
        case ON:
            return State.ON.ordinal();
        case OFF:
            return State.OFF.ordinal();
        case BUSY:
            return State.BUSY.ordinal();
        case SILENCE:
            return State.SILENCE.ordinal();
        default:
            return 0;
        }
    }

    public static State convertIntToStateEnum(int state) {
        return State.values()[state];
    }

    public static int convertPhoneTypeEnumToInt(PhoneType phone) {

        switch (phone) {
        case GEN2:
            return PhoneType.GEN2.ordinal();
        case GEN3:
            return PhoneType.GEN3.ordinal();
        default:
            return 0;
        }
    }

    public static PhoneType convertIntToPhoneTypeEnum(int phoneType) {
        return PhoneType.values()[phoneType];
    }

    public static int convertCommunicationTypeEnumToInt(CommunicationType communication) {

        switch (communication) {
        case SMS:
            return CommunicationType.SMS.ordinal();
        case VOICE:
            return CommunicationType.VOICE.ordinal();
        case VIDEO:
            return CommunicationType.VIDEO.ordinal();
        default:
            return 0;
        }
    }

    public static CommunicationType convertIntToCommunicationTypeEnum(int communicationType) {
        return CommunicationType.values()[communicationType];
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static int getCommunicationDuration(long startTime) {
        return ((int) (getCurrentTime() - startTime)) / 1000;
    }
}
