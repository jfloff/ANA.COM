package pt.ist.anacom.shared.exception;

public class PhoneAndOperatorPrefixDoNotMatchException extends PhoneException {

    private static final long serialVersionUID = 1L;

    private String operatorPrefix;

    public PhoneAndOperatorPrefixDoNotMatchException() {

    }

    public PhoneAndOperatorPrefixDoNotMatchException(String operatorPrefix, String phoneNumber) {
        super("Phone [" + phoneNumber + "] prefix does not match operator prefix [" + operatorPrefix + "].", phoneNumber);
        this.operatorPrefix = operatorPrefix;
    }

    public String getOperatorPrefix() {
        return this.operatorPrefix;
    }
}
