package pt.ist.anacom.shared.exception;

public class InsuficientBalanceException extends PhoneException {

    private static final long serialVersionUID = 1L;

    private int balance;

    public InsuficientBalanceException() {

    }

    public InsuficientBalanceException(String phoneNumber, int balance) {
        super("Phone [" + phoneNumber + "] doesn't have enough balance [" + balance + "].", phoneNumber);
        this.balance = balance;
    }

    public int getPhoneBalance() {
        return this.balance;
    }
}
