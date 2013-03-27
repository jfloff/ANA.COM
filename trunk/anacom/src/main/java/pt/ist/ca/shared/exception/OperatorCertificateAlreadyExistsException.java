package pt.ist.ca.shared.exception;

public class OperatorCertificateAlreadyExistsException extends CertificateAuthorityException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String operatorID;

    public OperatorCertificateAlreadyExistsException() {
    }

    public OperatorCertificateAlreadyExistsException(String operatorID) {
        super("Operator [" + operatorID + "] already has an valid certificate.");
        this.operatorID = operatorID;
    }

    public String getOperatorID() {
        return operatorID;
    }
}
