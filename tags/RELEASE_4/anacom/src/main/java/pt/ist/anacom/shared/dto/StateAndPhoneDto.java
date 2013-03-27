package pt.ist.anacom.shared.dto;

import java.io.Serializable;

import pt.ist.anacom.shared.data.AnacomData;

public class StateAndPhoneDto extends StateDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String phoneNumber;

    public StateAndPhoneDto() {

    }

    public StateAndPhoneDto(String phoneNumber, AnacomData.State state) {
        super(state);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String toString() {
        return "[Phone: " + this.phoneNumber + ", Status: " + super.getState().name() + "]";
    }
}
