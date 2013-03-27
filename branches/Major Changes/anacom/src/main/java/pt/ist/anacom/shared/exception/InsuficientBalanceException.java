package pt.ist.anacom.shared.exception;

public class InsuficientBalanceException extends PhoneException {

    private static final long serialVersionUID = 1L;

    private String phoneNumber;
    private int phoneBalance;

    public InsuficientBalanceException() {

    }

    public InsuficientBalanceException(String number, int balance) {
        super("Insuficient Balance [" + number + " has " + balance + "]");
        this.phoneNumber = number;
        this.phoneBalance = balance;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getPhoneBalance() {
        return phoneBalance;
    }

}
