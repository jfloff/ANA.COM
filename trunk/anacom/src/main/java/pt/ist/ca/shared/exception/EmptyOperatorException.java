package pt.ist.ca.shared.exception;

public class EmptyOperatorException extends CertificateAuthorityException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EmptyOperatorException() {
        super("Empty certificate operator ID.");
    }
}
