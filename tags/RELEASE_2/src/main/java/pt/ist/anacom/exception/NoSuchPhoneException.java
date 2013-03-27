package pt.ist.anacom.exception;

public class NoSuchPhoneException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String number;

	public NoSuchPhoneException(String number) {
		super("Phone does not exist [" + number + "]");
		this.number = number;
	}

	public String getNumber() {
		return this.number;
	}

}
