package pt.ist.anacom.shared.exception;

public class InvalidTaxException extends AnacomException {

    private static final long serialVersionUID = 1L;

    public InvalidTaxException() {
        super("Invalid Tax value (must be grater than 0)");
    }

}
