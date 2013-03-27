package pt.ist.anacom.shared.exception;

public class NegativeBalanceValueException extends PhoneException {

    private static final long serialVersionUID = 1L;

    private int balanceValue;

    public NegativeBalanceValueException() {

    }

    public NegativeBalanceValueException(String phoneNumber, int balanceValue) {
        super("Phone [" + phoneNumber + "] balance can't be negative [" + balanceValue + "].", phoneNumber);
    }

    public int getBalance() {
        return this.balanceValue;
    }
}
