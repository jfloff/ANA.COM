package pt.ist.ca.shared.exception;

public class CertificateDoesNotExistException extends CertificateAuthorityException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String operatorID;

    public CertificateDoesNotExistException() {
    }

    public CertificateDoesNotExistException(String operatorID) {
        super("Operator [" + operatorID + "] doesnt have an associated certificate.");
        this.operatorID = operatorID;
    }

    public String getOperatorID() {
        return this.operatorID;
    }
}
