package pt.ist.anacom.service;

import jvstm.Atomic;
import pt.ist.anacom.shared.exception.AnacomException;

public abstract class AnacomService {

    @Atomic
    public void execute() throws AnacomException {
        dispatch();
    }

    public abstract void dispatch() throws AnacomException;
}
