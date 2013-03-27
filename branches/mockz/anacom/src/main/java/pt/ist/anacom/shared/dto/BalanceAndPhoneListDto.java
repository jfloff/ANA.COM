package pt.ist.anacom.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class BalanceAndPhoneListDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private ArrayList<BalanceAndPhoneDto> balanceAndPhoneList;

    public BalanceAndPhoneListDto() {
        balanceAndPhoneList = new ArrayList<BalanceAndPhoneDto>();
    }

    public void add(String phoneNumber, int balance) {
        balanceAndPhoneList.add(new BalanceAndPhoneDto(phoneNumber, balance));
    }

    public ArrayList<BalanceAndPhoneDto> getPhoneList() {
        return this.balanceAndPhoneList;
    }

    @Override
    public String toString() {
        return balanceAndPhoneList.toString();
    }

}
