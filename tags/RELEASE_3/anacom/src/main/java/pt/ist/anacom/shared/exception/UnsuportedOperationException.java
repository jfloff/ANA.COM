package pt.ist.anacom.shared.exception;

public class UnsuportedOperationException extends AnacomException {

	private static final long serialVersionUID = 1L;

	public UnsuportedOperationException() {
		super("Unsuported operation.");
	}

}
