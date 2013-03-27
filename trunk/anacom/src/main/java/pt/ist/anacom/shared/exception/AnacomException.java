package pt.ist.anacom.shared.exception;

public abstract class AnacomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AnacomException() {

    }

    public AnacomException(String string) {
        super("EXCEPTION:ANACOM:" + string);
    }
}
