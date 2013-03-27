package pt.ist.anacom.shared.exception;

public class PhoneException extends AnacomException {

    private static final long serialVersionUID = 1L;

    public PhoneException() {

    }

    public PhoneException(String string) {
        super("PHONE EXCEPTION: " + string);
    }

}
