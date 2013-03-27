package pt.ist.anacom.shared.dto;

import java.io.Serializable;

import pt.ist.anacom.shared.data.AnacomData;

public class StateDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int state;

    public StateDto() {

    }

    public StateDto(AnacomData.State state) {
        this.state = state.ordinal();
    }

    public AnacomData.State getState() {
        return AnacomData.convertIntToStateEnum(this.state);
    }

    public String toString() {
        return "[Status:" + this.getState() + "]";
    }
}
