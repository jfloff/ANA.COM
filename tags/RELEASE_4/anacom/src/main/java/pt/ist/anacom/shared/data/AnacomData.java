package pt.ist.anacom.shared.data;

public class AnacomData {

    /*
     * ------------------- Data Values -------------------
     */

    public static final int PREFIX_POS = 0;
    public static final int PREFIX_LENGTH = 2;
    public static final int NUMBER_LENGTH = 9;

    /*
     * ------------------- Enums -------------------
     */

    public static enum State {
        ON, OFF, BUSY, SILENCE;
    }

    public static enum PhoneType {
        GEN2, GEN3
    }

}
