package pt.ist.phonebook.shared.exception;

public class DirectAcessNotAllowedWException extends PhoneBookException {

    private static final long serialVersionUID = 1L;

    public DirectAcessNotAllowedWException() {
    }

    @Override
    public String toString() {
        return "You have no premission to execute this method.";
    }

}
