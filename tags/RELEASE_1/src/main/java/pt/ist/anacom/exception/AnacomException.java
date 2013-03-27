package pt.ist.anacom.exception;

public abstract class AnacomException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AnacomException(String string) {
		super(string);
	}

}
