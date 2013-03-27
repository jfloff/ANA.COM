package pt.ist.anacom.shared.exception;

public class PhoneIsSILENCEException extends PhoneException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    public PhoneIsSILENCEException() {

    }

    public PhoneIsSILENCEException(String phoneNumber) {
        super("The phone number " + phoneNumber + " is in silence.");
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNr() {
        return phoneNumber;
    }

}
