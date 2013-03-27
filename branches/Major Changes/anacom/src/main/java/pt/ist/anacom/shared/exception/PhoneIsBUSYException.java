package pt.ist.anacom.shared.exception;

public class PhoneIsBUSYException extends PhoneException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    public PhoneIsBUSYException() {

    }

    public PhoneIsBUSYException(String phoneNumber) {
        super("The phone number " + phoneNumber + " is busy.");
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNr() {
        return phoneNumber;
    }
}
