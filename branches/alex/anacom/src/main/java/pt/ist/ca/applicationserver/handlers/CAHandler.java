package pt.ist.ca.applicationserver.handlers;

import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.util.Properties;
import java.util.Set;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ist.ca.applicationserver.CertificateAuthorityWebService;
import pt.ist.shared.SecurityData;
import pt.ist.shared.SignedCertificate;

public class CAHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public void close(MessageContext messageContext) {
		return;
	}

	@Override
	public Set getHeaders() {
		return null;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {

		// Boolean outboundProperty = (Boolean) context
		// .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		//
		// if (!outboundProperty.booleanValue()) {
		//
		// System.out.println("CA Inbound SOAP message:");
		//
		// // try {
		// // SOAPMessage message = context.getMessage();
		// // SOAPPart soapPart = message.getSOAPPart();
		// // SOAPEnvelope soapEnvelope;
		// // soapEnvelope = soapPart.getEnvelope();
		// // SOAPHeader soapHeader = soapEnvelope.getHeader();
		// //
		// // if (soapHeader != null) {
		// //
		// // SOAPElement signature =
		// // SecurityData.getSOAPHeaderElement(soapHeader, "signature");
		// // if (signature != null) {
		// // String sig = signature.getValue();
		// // soapHeader.removeChild(signature);
		// //
		// // SOAPElement cert = SecurityData.getSOAPHeaderElement(soapHeader,
		// // "certificate");
		// // String certificateString = cert.getValue();
		// // soapHeader.removeChild(cert);
		// // byte[] certificateBytes =
		// // SecurityData.decode64(certificateString);
		// // SignedCertificate certificate = (SignedCertificate)
		// // SecurityData.deserialize(certificateBytes);
		// //
		// // byte[] result = SecurityData.decode64(sig);
		// // ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		// // message.writeTo(byteOut);
		// //
		// // String pubKey =
		// // certificate.getCertificateContents().getPublicKey();
		// // PublicKey publicKey = SecurityData.getPublicKey(pubKey);
		// //
		// // if (SecurityData.verifyDigitalSignature(result,
		// // byteOut.toByteArray(), publicKey))
		// // System.out.println("VALID SIGNATURE");
		// // else {
		// // System.out.println("NOT VALID SIGNATURE");
		// // return false;
		// // }
		// // }
		// //
		// // else {
		// // System.out.println("NOT SIGNATURE");
		// // }
		// // }
		// // } catch (SOAPException e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // } catch (Exception e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		// } else {
		// System.out.println("CA OUTBOUND MESSAGE");
		// // try {
		// //
		// // SOAPMessageContext messageContext = (SOAPMessageContext) context;
		// // SOAPMessage message = messageContext.getMessage();
		// // SOAPPart soapPart = message.getSOAPPart();
		// // SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		// // SOAPHeader soapHeader = soapEnvelope.getHeader();
		// // if (soapHeader == null) {
		// // soapHeader = soapEnvelope.addHeader();
		// // }
		// //
		// // Name name = soapEnvelope.createName("entity", "security",
		// // "http://ca.essd.0403");
		// // SOAPElement element = soapHeader.addHeaderElement(name);
		// // element.addTextNode(CertificateAuthorityWebService.name);
		// //
		// // Properties properties =
		// //
		// SecurityData.readPropertiesFile(CertificateAuthorityWebService.filePath);
		// // String privatePath = properties.getProperty("privateKeyCAPath") +
		// // "CAprivate.dat";
		// //
		// // ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		// // message.writeTo(byteOut);
		// //
		// // byte[] cipherDigest =
		// // SecurityData.makeDigitalSignature(byteOut.toByteArray(),
		// // SecurityData.readPrivateKeys(privatePath));
		// //
		// // String sig = SecurityData.encode64(cipherDigest);
		// //
		// // Name sigName = soapEnvelope.createName("signature", "security",
		// // "http://ca.essd.0403");
		// // SOAPElement sigElement = soapHeader.addHeaderElement(sigName);
		// // sigElement.addTextNode(sig);
		// //
		// // // SignedCertificate certificate =
		// // // CertificateAuthorityWebService.caCertificate;
		// // // byte[] certBytes = SecurityData.serialize(certificate);
		// // // Name certName = soapEnvelope.createName("certificate",
		// // "security",
		// // // "http://ca.essd.0403");
		// // // SOAPElement certElement =
		// // soapHeader.addHeaderElement(certName);
		// // // String certString = SecurityData.encode64(certBytes);
		// // // certElement.addTextNode(certString);
		// //
		// // } catch (Exception e) {
		// // System.out.println(e.getStackTrace());
		// // System.out.println(this +
		// // ">\n\tException caught in handleRequest:\n" + e);
		// // return false;
		// // }
		// }
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return false;
	}
}
