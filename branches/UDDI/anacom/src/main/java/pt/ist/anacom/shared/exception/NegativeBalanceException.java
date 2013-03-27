package pt.ist.anacom.shared.exception;

public class NegativeBalanceException extends PhoneException {

    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    public NegativeBalanceException() {
        super();
    }

    public NegativeBalanceException(String number) {
        super("Invalid Balance Operation. Balance of number " + number + " can't be negative.");
        this.phoneNumber = number;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
