package pt.ist.anacom.shared.exception;

public class InvalidTaxEception extends AnacomException {

    private static final long serialVersionUID = 1L;

    public InvalidTaxEception() {
        super("Invalid Tax value (must be grater than 0)");
    }

}
