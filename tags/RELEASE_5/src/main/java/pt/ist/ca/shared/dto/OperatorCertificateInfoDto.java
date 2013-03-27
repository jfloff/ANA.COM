package pt.ist.ca.shared.dto;

import java.io.Serializable;

public class OperatorCertificateInfoDto extends OperatorDto implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String publicKey;
    private int validity;

    public OperatorCertificateInfoDto() {
    }

    public OperatorCertificateInfoDto(String operatorID, String publicKey, int validity) {
        super(operatorID);
        this.publicKey = publicKey;
        this.validity = validity;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public int getValidity() {
        return this.validity;
    }
}
