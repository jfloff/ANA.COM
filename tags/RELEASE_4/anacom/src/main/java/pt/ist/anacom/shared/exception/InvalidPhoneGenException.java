package pt.ist.anacom.shared.exception;

public class InvalidPhoneGenException extends PhoneException {

    private static final long serialVersionUID = 1L;

    private String gen;

    public InvalidPhoneGenException() {

    }

    public InvalidPhoneGenException(String gen) {
        super("Invalid Phone Generation: " + gen + ".");
        this.gen = gen;
    }

    public String getPhoneGen() {
        return gen;
    }
}
