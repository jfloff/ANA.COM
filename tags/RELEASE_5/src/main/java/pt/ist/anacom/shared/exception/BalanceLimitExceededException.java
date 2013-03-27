package pt.ist.anacom.shared.exception;

public class BalanceLimitExceededException extends PhoneException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int balance;

    public BalanceLimitExceededException() {

    }

    public BalanceLimitExceededException(String phoneNumber, int balance) {
        super("Phone [" + phoneNumber + "] balance [" + balance + " ] must be at most 10000.", phoneNumber);
        this.balance = balance;
    }

    public int getPhoneBalance() {
        return this.balance;
    }
}
