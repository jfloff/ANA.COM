package pt.ist.anacom.exception;

public class OperatorDoesNotExistException extends AnacomException {

	private static final long serialVersionUID = 1L;

	private String name;

	public OperatorDoesNotExistException(String name) {
		super("Operator " + name + " does not exist");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
