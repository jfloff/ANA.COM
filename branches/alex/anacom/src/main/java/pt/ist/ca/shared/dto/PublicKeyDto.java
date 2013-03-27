package pt.ist.ca.shared.dto;

import java.io.Serializable;

public class PublicKeyDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String publicKey;

    public PublicKeyDto() {
    }

    public PublicKeyDto(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
