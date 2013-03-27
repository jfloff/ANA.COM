package pt.ist.anacom.shared.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

import pt.ist.anacom.applicationserver.ApplicationServerWebService;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.ca.shared.dto.OperatorCertificateInfoDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.shared.CertificateContents;
import pt.ist.shared.SecurityData;
import pt.ist.shared.SignedCertificate;

public class BlackListAndKeysThread extends Thread {

    private boolean run = true;

    public void run() {

        while (run) {

            String entity = ApplicationServerWebService.operatorINFO.entityID;
            Properties properties = SecurityData.readPropertiesFile(ApplicationServerWebService.operatorINFO.path);
            String publicPath = properties.getProperty("publicKeyPath") + entity + "public.dat";
            try {
                FileInputStream fin = new FileInputStream(publicPath);
                byte[] pubEncoded = new byte[fin.available()];
                fin.read(pubEncoded);

                String contents = SecurityData.encode64(ApplicationServerWebService.operatorINFO.getContents());
                String encoded = SecurityData.encode64(pubEncoded);

                if (!contents.equals(encoded)) {
                    PublicKey pubKey = SecurityData.readPublicKeys(publicPath);
                    ApplicationServerWebService.bridge.revokeCertificate(new OperatorDto(entity));
                    OperatorCertificateInfoDto dto = new OperatorCertificateInfoDto(entity, SecurityData.encode64(pubKey.getEncoded()),
                            SecurityData.VALIDITY);
                    CertificateDto certificateDto = ApplicationServerWebService.bridge.signCertificate(dto);
                    String certString = certificateDto.getCertificate();
                    String certSignature = certificateDto.getSignature();
                    CertificateContents certificateContents = (CertificateContents) SecurityData.deserialize(SecurityData.decode64(certString));
                    SignedCertificate actualCertificate = new SignedCertificate(certificateContents, certSignature);
                    ApplicationServerWebService.operatorINFO.setActualCertificate(actualCertificate);
                    ApplicationServerWebService.operatorINFO.setContains(pubEncoded);
                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // get black list
            CertificateListDto dto = ApplicationServerWebService.bridge.getRevokedCertificateList();

            for (CertificateDto certDto : dto.getCertificateList()) {
                String certString = certDto.getCertificate();
                try {
                    CertificateContents certContents = (CertificateContents) SecurityData.deserialize(SecurityData.decode64(certString));
                    SignedCertificate certificate = new SignedCertificate(certContents, certDto.getSignature());
                    ApplicationServerWebService.operatorINFO.addRevokedCertificate(certificate);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.interrupt();
    }

    public void stopThread() {
        this.run = false;
    }
}
