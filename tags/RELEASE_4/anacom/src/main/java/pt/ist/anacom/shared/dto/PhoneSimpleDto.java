package pt.ist.anacom.shared.dto;

import java.io.Serializable;

import pt.ist.anacom.shared.data.AnacomData;

public class PhoneSimpleDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    public PhoneSimpleDto() {

    }

    public PhoneSimpleDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getOperatorPrefix() {
        return this.phoneNumber.substring(AnacomData.PREFIX_POS, AnacomData.PREFIX_LENGTH);
    }

    @Override
    public String toString() {
        return "[Phone: " + phoneNumber + "]";
    }
}
