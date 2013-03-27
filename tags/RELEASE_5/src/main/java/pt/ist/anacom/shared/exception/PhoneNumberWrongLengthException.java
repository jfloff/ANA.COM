package pt.ist.anacom.shared.exception;

public class PhoneNumberWrongLengthException extends PhoneException {

    private static final long serialVersionUID = 1L;

    public PhoneNumberWrongLengthException() {

    }

    public PhoneNumberWrongLengthException(String phoneNumber) {
        super("Phone Number [" + phoneNumber + "] must have 9 digits.", phoneNumber);
    }
}
