package pt.ist.anacom.shared.exception;

import pt.ist.anacom.shared.data.AnacomData;

public class PhoneStateException extends PhoneException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private AnacomData.State state;

    public PhoneStateException() {

    }

    public PhoneStateException(String phoneNumber, AnacomData.State state) {
        super("Phone [" + phoneNumber + "] is [" + state.name() + "], can't make communication.", phoneNumber);
        this.state = state;
    }

    public AnacomData.State getState() {
        return this.state;
    }
}
