package pt.ist.ca.service;

import java.io.IOException;

import jvstm.Atomic;
import pt.ist.ca.shared.exception.CertificateAuthorityException;

public abstract class CertificateAuthorityService {

    @Atomic
    public void execute() throws CertificateAuthorityException, IOException {
        dispatch();
    }

    public abstract void dispatch() throws CertificateAuthorityException, IOException;
}
