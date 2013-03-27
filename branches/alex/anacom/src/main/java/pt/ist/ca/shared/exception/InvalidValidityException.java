package pt.ist.ca.shared.exception;

public class InvalidValidityException extends CertificateAuthorityException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int validity;

    public InvalidValidityException() {
    }

    public InvalidValidityException(int validity) {
        super("Invalid certificate validity [" + validity + "] must be more than 0.");
        this.validity = validity;
    }

    public int getValidity() {
        return this.validity;
    }
}
