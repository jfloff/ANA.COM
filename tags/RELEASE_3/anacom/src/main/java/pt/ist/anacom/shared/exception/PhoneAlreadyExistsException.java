package pt.ist.anacom.shared.exception;

public class PhoneAlreadyExistsException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String phoneNumber;

	public PhoneAlreadyExistsException(String phoneNumber) {
		super("Duplicate Phone [" + phoneNumber + "]");
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

}
