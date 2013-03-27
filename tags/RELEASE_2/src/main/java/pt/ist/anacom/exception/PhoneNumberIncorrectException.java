package pt.ist.anacom.exception;

public class PhoneNumberIncorrectException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String number;

	public PhoneNumberIncorrectException(String number) {
		super("Incorrect Phone Number [" + number + "]");
		this.number = number;
	}

	public String getNumber() {
		return number;
	}
}
