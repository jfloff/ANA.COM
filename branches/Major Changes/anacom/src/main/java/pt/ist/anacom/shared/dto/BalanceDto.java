package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class BalanceDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int balance;

    public BalanceDto() {
    }

    public BalanceDto(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "[Bal: " + balance + "]";
    }

}
