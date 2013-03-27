package pt.ist.anacom.shared.security;

import java.io.IOException;

import pt.ist.anacom.applicationserver.ApplicationServerWebService;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.shared.CertificateContents;
import pt.ist.shared.SecurityData;
import pt.ist.shared.SignedCertificate;

public class BlackListAndKeysThread extends Thread {

	private boolean run = true;

	public void run() {

		while (run) {
			// get black list
			CertificateListDto dto = ApplicationServerWebService.bridge
					.getRevokedCertificateList();

			for (CertificateDto certDto : dto.getCertificateList()) {
				String certString = certDto.getCertificate();
				try {
					CertificateContents certContents = (CertificateContents) SecurityData
							.deserialize(SecurityData.decode64(certString));
					SignedCertificate certificate = new SignedCertificate(
							certContents, certDto.getSignature());
					ApplicationServerWebService.operatorINFO
							.addRevokedCertificate(certificate);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			System.out.println("AFTER UPDATING LIST!");

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