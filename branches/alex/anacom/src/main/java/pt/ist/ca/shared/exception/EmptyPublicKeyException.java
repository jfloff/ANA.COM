package pt.ist.ca.shared.exception;

public class EmptyPublicKeyException extends CertificateAuthorityException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EmptyPublicKeyException() {
        super("Empty certificate public key.");
    }
}
