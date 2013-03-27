package pt.ist.anacom.exception;

public class UnsuportedOperationException extends AnacomException {

	private static final long serialVersionUID = 1L;

	public UnsuportedOperationException() {
		super("Unsuported operation.");
	}

}
