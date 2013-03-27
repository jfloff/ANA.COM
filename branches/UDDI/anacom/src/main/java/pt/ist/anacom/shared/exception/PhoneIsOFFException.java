package pt.ist.anacom.shared.exception;

public class PhoneIsOFFException extends PhoneException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    public PhoneIsOFFException() {

    }

    public PhoneIsOFFException(String phoneNumber) {
        super("The phone number " + phoneNumber + " is turned off.");
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
