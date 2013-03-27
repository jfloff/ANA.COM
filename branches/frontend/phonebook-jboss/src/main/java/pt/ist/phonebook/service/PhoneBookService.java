package pt.ist.phonebook.service;

import pt.ist.phonebook.shared.exception.PhoneBookException;
import jvstm.Atomic;

public abstract class PhoneBookService {

    @Atomic
    public void execute() throws PhoneBookException {
        dispatch();
    }

    public abstract void dispatch() throws PhoneBookException;
}
