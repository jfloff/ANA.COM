package pt.ist.ca.shared.dto;

import java.io.Serializable;

public class OperatorAndKeyDto extends OperatorDto implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String publicKey;
    private long validity;

    public OperatorAndKeyDto() {
    }

    public OperatorAndKeyDto(String operatorID, String publicKey, long validity) {
        super(operatorID);
        this.publicKey = publicKey;
        this.validity = validity;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public long getValidity() {
        return this.validity;
    }
}
