package pt.ist.ca.shared.dto;

import java.io.Serializable;

public class OperatorDto implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String operatorID;

    public OperatorDto() {
    }

    public OperatorDto(String operatorID) {
        this.operatorID = operatorID;
    }

    public String getOperatorID() {
        return operatorID;
    }
}
