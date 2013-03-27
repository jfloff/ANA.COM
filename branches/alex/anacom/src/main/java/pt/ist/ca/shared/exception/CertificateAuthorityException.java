package pt.ist.ca.shared.exception;

public abstract class CertificateAuthorityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CertificateAuthorityException() {

    }

    public CertificateAuthorityException(String string) {
        super("EXCEPTION: CERTIFICATE AUTHORITY:" + string);
    }
}
