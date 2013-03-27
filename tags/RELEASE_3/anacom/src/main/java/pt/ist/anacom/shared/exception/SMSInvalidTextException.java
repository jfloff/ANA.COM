package pt.ist.anacom.shared.exception;

public class SMSInvalidTextException extends AnacomException {

	private static final long serialVersionUID = 1L;

	public SMSInvalidTextException() {
		super("Invalid SMS Text (null).");
	}
}
