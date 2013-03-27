package pt.ist.anacom.shared.security;

import java.util.ArrayList;
import java.util.List;

import pt.ist.shared.SignedCertificate;

public class EntityINFO {

    public String path;
    public String entityID;
    public String caPublicKey;
    public SignedCertificate actualCertificate;
    public List<SignedCertificate> revokedList;

    public EntityINFO(String entityID, String path) {
        this.entityID = entityID;
        this.path = path;
        this.revokedList = new ArrayList<SignedCertificate>();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public String getCaPublicKey() {
        return caPublicKey;
    }

    public void setCaPublicKey(String caPublicKey) {
        this.caPublicKey = caPublicKey;
    }

    public SignedCertificate getActualCertificate() {
        return actualCertificate;
    }

    public void setActualCertificate(SignedCertificate actualCertificate) {
        this.actualCertificate = actualCertificate;
    }

    public List<SignedCertificate> getRevokedList() {
        return revokedList;
    }

    public void setRevokedList(List<SignedCertificate> revokedList) {
        this.revokedList = revokedList;
    }
}
