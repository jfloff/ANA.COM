package pt.ist.ca.shared.dto;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/* Provavelmente é para eliminar */
public class KeysDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private KeyPair keyPair;

    public KeysDto() {
    }

    public KeysDto(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public PublicKey getPublicKey() {
        return this.keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.keyPair.getPrivate();
    }
}
