package pt.ist.phonebook.shared.exception;

import java.util.HashMap;

public class SingletonExceptionTable {

    private final HashMap<Exception, PhoneBookException> exceptionTable = new HashMap<Exception, PhoneBookException>();
    private SingletonExceptionTable instance = null;

    protected SingletonExceptionTable() {

    }

    public SingletonExceptionTable getInstance() {
        if (instance == null)
            instance = new SingletonExceptionTable();
        return instance;
    }
}
