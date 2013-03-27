package pt.ist.ca.shared.exception;

public class CertificateDoesNotExistException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CertificateDoesNotExistException() {
        super();
    }

    public CertificateDoesNotExistException(String message) {
        super(message);
    }
}
