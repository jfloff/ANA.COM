package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class BalanceAndPhoneDto extends BalanceDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    public BalanceAndPhoneDto() {
    }

    public BalanceAndPhoneDto(String phoneNumber, int balance) {
        super(balance);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "[Phone: " + this.phoneNumber + ", Bal: " + super.getBalance() + "]";
    }

}
