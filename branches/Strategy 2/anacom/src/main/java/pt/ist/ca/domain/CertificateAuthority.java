package pt.ist.ca.domain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.ist.ca.shared.data.Data;
import pt.ist.ca.shared.exception.CertificateDoesNotExistException;

public class CertificateAuthority extends CertificateAuthority_Base {

    public CertificateAuthority() {
        super();
    }

    public Certificate getValidCertificate(String operatorID) {

        for (Certificate cert : this.getValidCertificate())
            if (cert.getCertificateINFO().getOperatorID().equals(operatorID))
                return cert;

        return null;
    }

    public List<Certificate> getRevokedCertificates(String operatorID) {

        List<Certificate> revokedList = new ArrayList<Certificate>();

        for (Certificate cert : this.getRevokedCertificate())
            if (cert.getCertificateINFO().getOperatorID().equals(operatorID))
                revokedList.add(cert);

        return revokedList;
    }

    public void revokeCertificate(String operatorID) throws CertificateDoesNotExistException {
        Certificate certificate = this.getValidCertificate(operatorID);
        if (certificate == null)
            throw new CertificateDoesNotExistException();
        addRevokedCertificate(certificate);
        removeValidCertificate(certificate);
    }

    public Certificate generateCertificate(String operatorID, String publicKey, long validity) throws Exception {

        Date date = new Date();
        String serialID = "#" + date.getTime() + date.toString() + "#";
        CertificateINFO certInfo = new CertificateINFO(serialID, operatorID, publicKey, validity);

        byte[] certInfoEncoded = Data.serialize(certInfo);

        byte[] signature = Data.makeDigitalSignature(certInfoEncoded, getPrivateKey());

        Certificate certificate = new Certificate(certInfo, Data.encode64(signature));

        this.addValidCertificate(certificate);

        return certificate;
    }

    public void addKeys(PublicKey pubKey, PrivateKey privKey) {
        this.setPublicKey(Data.encode64(pubKey.getEncoded()));
        this.setPrivateKey(Data.encode64(privKey.getEncoded()));
    }
}
