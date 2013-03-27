package pt.ist.anacom.presentationserver.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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

import pt.ist.anacom.presentationserver.PresentationServer;
import pt.ist.ca.shared.dto.CertificateDto;
import pt.ist.ca.shared.dto.CertificateListDto;
import pt.ist.ca.shared.dto.OperatorDto;
import pt.ist.shared.CertificateContents;
import pt.ist.shared.SecurityData;
import pt.ist.shared.SignedCertificate;

public class ClientHandler implements SOAPHandler<SOAPMessageContext> {

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
        Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (!outboundProperty.booleanValue()) {
            System.out.println("Client Inbound SOAP message:");

            try {
                SOAPMessage message = context.getMessage();
                SOAPPart soapPart = message.getSOAPPart();
                SOAPEnvelope soapEnvelope;
                soapEnvelope = soapPart.getEnvelope();
                SOAPHeader soapHeader = soapEnvelope.getHeader();

                if (soapHeader != null) {

                    SOAPElement entity = SecurityData.getSOAPHeaderElement(soapHeader, "entity");
                    String entityName = entity.getValue();

                    SOAPElement signature = SecurityData.getSOAPHeaderElement(soapHeader, "signature");

                    if (signature != null) {
                        String sig = signature.getValue();
                        soapHeader.removeChild(signature);

                        SOAPElement cert = SecurityData.getSOAPHeaderElement(soapHeader, "certificate");
                        String certificateString = cert.getValue();
                        soapHeader.removeChild(cert);
                        byte[] certificateBytes = SecurityData.decode64(certificateString);
                        SignedCertificate certificate = (SignedCertificate) SecurityData.deserialize(certificateBytes);
                        CertificateContents contents = certificate.getCertificateContents();

						// OperatorDto dto = new OperatorDto(entityName);
						// CertificateListDto listDto =
						// PresentationServer.certificateAuthority.getOperatorRevokedCertificateList(dto);
						//
						// for (CertificateDto certDto :
						// listDto.getCertificateList()) {
						// CertificateContents certContents =
						// (CertificateContents)
						// SecurityData.deserialize(SecurityData.decode64(certDto.getCertificate()));
						// SignedCertificate signed = new
						// SignedCertificate(certContents,
						// certDto.getSignature());
						// PresentationServer.clientINFO.getRevokedList().add(signed);
						// }
						//
						// if
						// (!PresentationServer.clientINFO.getRevokedList().contains(certificate))
						// System.out.println("Certificate Not Revoked");
						// else {
						// System.out.println("CERTIFICATE REVOKED");
						// return false;
						// }
                        if (SecurityData.checkCertificateValidity(contents.getStartDate(), contents.getEndDate()))
                            System.out.println("Certificate VALID");
                        else {
                            System.out.println("Certificate NOT VALID");
                            return false;
                        }
                        byte[] contentsBytes = SecurityData.serialize(contents);

                        if (SecurityData.verifyDigitalSignature(SecurityData.decode64(certificate.getSignature()),
                                                                contentsBytes,
                                                                SecurityData.getPublicKey(PresentationServer.clientINFO.getCaPublicKey())))
                            System.out.println("CA's SIGNATURES VALID");
                        else {
                            System.out.println("CA'S SIGNATURE NOT VALID");
                            return false;
                        }
                        byte[] result = SecurityData.decode64(sig);

                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        message.writeTo(byteOut);

                        String pubKey = certificate.getCertificateContents().getPublicKey();
                        PublicKey publicKey = SecurityData.getPublicKey(pubKey);

                        if (SecurityData.verifyDigitalSignature(result, byteOut.toByteArray(), publicKey))
                            System.out.println("VALID SIGNATURE!");

                        else {
                            System.out.println("NOT VALID SIGNATURE");
                            return false;
                        }
                    }
                }

            } catch (SOAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("CLIENT OUTBOUND MESSAGE");

            try {
                SOAPMessageContext messageContext = (SOAPMessageContext) context;
                SOAPMessage message = messageContext.getMessage();
                SOAPPart soapPart = message.getSOAPPart();
                SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
                SOAPHeader soapHeader = soapEnvelope.getHeader();
                if (soapHeader == null) {
                    soapHeader = soapEnvelope.addHeader();
                }

                Name name = soapEnvelope.createName("entity", "security", "http://essd.0403");
                SOAPElement element = soapHeader.addHeaderElement(name);
                element.addTextNode(PresentationServer.clientINFO.getEntityID());

                Properties prop = SecurityData.readPropertiesFile(PresentationServer.clientINFO.getPath());
                PrivateKey privateKey = SecurityData.readPrivateKeys(prop.getProperty("privateKeyPath")
                        + PresentationServer.clientINFO.getEntityID() + "private.dat");


                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                message.writeTo(byteOut);

                byte[] cipherDigest = SecurityData.makeDigitalSignature(byteOut.toByteArray(), privateKey);

                String sig = SecurityData.encode64(cipherDigest);

                Name sigName = soapEnvelope.createName("signature", "security", "http://essd.0403");
                SOAPElement sigElement = soapHeader.addHeaderElement(sigName);
                sigElement.addTextNode(sig);

                SignedCertificate certificate = PresentationServer.clientINFO.getActualCertificate();
                byte[] certBytes = SecurityData.serialize(certificate);

                Name certName = soapEnvelope.createName("certificate", "security", "http://essd.0403");
                SOAPElement certElement = soapHeader.addHeaderElement(certName);
                String certString = SecurityData.encode64(certBytes);
                certElement.addTextNode(certString);

            } catch (Exception e) {
                System.err.println(e.getStackTrace());
                System.out.println(this + ">\n\tException caught in handleRequest:\n" + e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }
}
