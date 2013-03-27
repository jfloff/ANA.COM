package pt.ist.anacom.shared.exception;

public class NoSuchPhoneException extends PhoneException {

    private static final long serialVersionUID = 1L;

    public NoSuchPhoneException() {

    }

    public NoSuchPhoneException(String phoneNumber) {
        super("Phone [" + phoneNumber + "] does not exist.", phoneNumber);
    }
}
