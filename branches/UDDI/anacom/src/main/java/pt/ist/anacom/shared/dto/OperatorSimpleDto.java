package pt.ist.anacom.shared.dto;

import java.io.Serializable;

public class OperatorSimpleDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private String operatorPrefix;

    public OperatorSimpleDto() {

    }

    public OperatorSimpleDto(String operatorPrefix) {
        this.operatorPrefix = operatorPrefix;
    }

    public String getOperatorPrefix() {
        return operatorPrefix;
    }

    @Override
    public String toString() {
        return "[Op: " + this.operatorPrefix + "]";
    }
}
