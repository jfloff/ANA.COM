package pt.ist.ca.shared.exception;

public class UnableToProvideServiceException extends CertificateAuthorityException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UnableToProvideServiceException() {
        super("Unbale to provide service.");
    }
}
