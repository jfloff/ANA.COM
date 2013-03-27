package pt.ist.anacom.shared.exception;

public class PhoneAlreadyExistsException extends PhoneException {

    private static final long serialVersionUID = 1L;

    public PhoneAlreadyExistsException() {

    }

    public PhoneAlreadyExistsException(String phoneNumber) {
        super("Phone already exists [" + phoneNumber + "].", phoneNumber);
    }
}
