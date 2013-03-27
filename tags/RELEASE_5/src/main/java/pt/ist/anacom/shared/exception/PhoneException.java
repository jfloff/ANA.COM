package pt.ist.anacom.shared.exception;

public class PhoneException extends AnacomException {

    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    public PhoneException() {

    }

    public PhoneException(String message) {
        super("PHONE:" + message);
    }

    public PhoneException(String message, String phoneNumber) {
        super("PHONE:" + message);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
}
