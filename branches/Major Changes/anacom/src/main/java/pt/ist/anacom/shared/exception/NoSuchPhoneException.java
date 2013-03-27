package pt.ist.anacom.shared.exception;

public class NoSuchPhoneException extends PhoneException {

    private static final long serialVersionUID = 1L;

    private String number;

    public NoSuchPhoneException() {

    }

    public NoSuchPhoneException(String number) {
        super("Phone does not exist [" + number + "]");
        this.number = number;
    }

    public String getNumber() {
        return this.number;
    }

}
