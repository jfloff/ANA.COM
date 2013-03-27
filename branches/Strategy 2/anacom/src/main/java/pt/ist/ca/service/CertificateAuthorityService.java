package pt.ist.ca.service;

import jvstm.Atomic;

public abstract class CertificateAuthorityService {

    @Atomic
    public void execute() throws Exception {
        dispatch();
    }

    public abstract void dispatch() throws Exception;
}
