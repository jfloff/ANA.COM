package pt.ist.anacom.shared.exception;

public class OperatorException extends AnacomException {

    private static final long serialVersionUID = 1L;

    public OperatorException() {

    }

    public OperatorException(String string) {
        super("OPERATOR EXCEPTION: " + string);
    }

}
