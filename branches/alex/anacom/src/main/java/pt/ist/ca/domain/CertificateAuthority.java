package pt.ist.ca.domain;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import pt.ist.ca.shared.exception.CertificateDoesNotExistException;
import pt.ist.ca.shared.exception.EmptyOperatorException;
import pt.ist.ca.shared.exception.EmptyPublicKeyException;
import pt.ist.ca.shared.exception.InvalidValidityException;
import pt.ist.ca.shared.exception.UnableToProvideServiceException;
import pt.ist.shared.CertificateContents;
import pt.ist.shared.SecurityData;
import pt.ist.shared.SignedCertificate;

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

    public List<SignedCertificate> getRevokedCertificates() {

        List<SignedCertificate> revokedList = new ArrayList<SignedCertificate>();

        for (Certificate cert : this.getRevokedCertificate()) {
            CertificateINFO certINFO = cert.getCertificateINFO();
                CertificateContents contents = new CertificateContents(certINFO.getSerialID(), certINFO.getOperatorID(), certINFO.getPublicKey(),
                        certINFO.getStartDate(), certINFO.getEndDate());
                revokedList.add(new SignedCertificate(contents, cert.getSignature()));
        }
    
        return revokedList;
    }

    public void revokeCertificate(String operatorID) {
        Certificate certificate = this.getValidCertificate(operatorID);
        if (certificate == null)
            throw new CertificateDoesNotExistException(operatorID);
        addRevokedCertificate(certificate);
        removeValidCertificate(certificate);
    }

    public SignedCertificate generateCertificate(String operatorID, String publicKey, int validity) {

        Date date = new Date();
        String serialID = "#" + date.getTime() + date.toString() + "#";

        if (operatorID.isEmpty()) {
            throw new EmptyOperatorException();
        }

        if (publicKey.isEmpty()) {
            throw new EmptyPublicKeyException();
        }

        if (validity <= 0) {
            throw new InvalidValidityException(validity);
        }

        Certificate cert = this.getValidCertificate(operatorID);

        if (cert != null)
            revokeCertificate(operatorID);

        CertificateINFO certInfo = new CertificateINFO(serialID, operatorID, publicKey, validity);
        CertificateContents certContents = new CertificateContents(serialID, operatorID, publicKey, certInfo.getStartDate(), certInfo.getEndDate());

        byte[] certInfoEncoded;
        byte[] signature;

        try {
            certInfoEncoded = SecurityData.serialize(certContents);
            signature = SecurityData.makeDigitalSignature(certInfoEncoded, SecurityData.getPrivateKey(this.getPrivateKey()));
        } catch (InvalidKeyException e) {
            throw new UnableToProvideServiceException();
        } catch (NoSuchAlgorithmException e) {
            throw new UnableToProvideServiceException();
        } catch (NoSuchPaddingException e) {
            throw new UnableToProvideServiceException();
        } catch (IllegalBlockSizeException e) {
            throw new UnableToProvideServiceException();
        } catch (BadPaddingException e) {
            throw new UnableToProvideServiceException();
        } catch (InvalidKeySpecException e) {
            throw new UnableToProvideServiceException();
        } catch (IOException e) {
            throw new UnableToProvideServiceException();
        }

        String sig = SecurityData.encode64(signature);
        Certificate certificate = new Certificate(certInfo, sig);
        this.addValidCertificate(certificate);

        return new SignedCertificate(certContents, sig);
    }

    public void addKeys(PublicKey pubKey, PrivateKey privKey) {
        this.setPublicKey(SecurityData.encode64(pubKey.getEncoded()));
        this.setPrivateKey(SecurityData.encode64(privKey.getEncoded()));
    }
}
