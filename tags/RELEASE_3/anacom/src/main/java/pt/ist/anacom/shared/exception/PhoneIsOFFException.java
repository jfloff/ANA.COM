package pt.ist.anacom.shared.exception;

public class PhoneIsOFFException extends AnacomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String phoneNumber;

	public PhoneIsOFFException(String phoneNumber) {
		super("The phone number " + phoneNumber + " is turned off.");
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNr() {
		return phoneNumber;
	}

}
