package pt.ist.anacom.shared.exception;

public class PhoneNumberIncorrectException extends PhoneException {

    private static final long serialVersionUID = 1L;

    private String number;

    public PhoneNumberIncorrectException() {

    }

    public PhoneNumberIncorrectException(String number) {
        super("Incorrect Phone Number [" + number + "]");
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
