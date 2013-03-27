package pt.ist.anacom.exception;

public class SMSInvalidTextException extends AnacomException {

	private static final long serialVersionUID = 1L;

	public SMSInvalidTextException() {
		super("Invalid SMS Text (null).");
	}

}
