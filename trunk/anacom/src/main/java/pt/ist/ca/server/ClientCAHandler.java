package pt.ist.ca.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.namespace.QName;
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

import pt.ist.anacom.shared.security.EntityINFO;
import pt.ist.shared.SecurityData;
import pt.ist.shared.SignedCertificate;

public class ClientCAHandler implements SOAPHandler<SOAPMessageContext> {

    private EntityINFO entityINFO;

    public ClientCAHandler(EntityINFO entityINFO) {
        this.entityINFO = entityINFO;
    }

    @Override
    public void close(MessageContext messageContext) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        // Inbound message
        if (!outboundProperty.booleanValue()) {
            System.out.println("CLIENT CA HANDLER INBOUND");
            try {
                SOAPMessage message = messageContext.getMessage();
                SOAPPart soapPart = message.getSOAPPart();
                SOAPEnvelope soapEnvelope;
                soapEnvelope = soapPart.getEnvelope();
                SOAPHeader soapHeader = soapEnvelope.getHeader();

                if (soapHeader != null) {

                    SOAPElement signature = SecurityData.getSOAPHeaderElement(soapHeader, "signature");
                    if (signature != null) {
                        String sig = signature.getValue();
                        soapHeader.removeChild(signature);

                        byte[] result = SecurityData.decode64(sig);
                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        message.writeTo(byteOut);

                        PublicKey publicKey = SecurityData.getPublicKey(entityINFO.getCaPublicKey());

                        if (SecurityData.verifyDigitalSignature(result, byteOut.toByteArray(), publicKey))
                            System.out.println("VALID SIGNATURE");
                        else {
                            System.out.println("NOT VALID SIGNATURE");
                            return false;
                        }
                    }
                }
            } catch (SOAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        // Outbound message
        else {
            System.out.println("CLIENT CA HANDLER OUTBOUND");
            try {
                SOAPMessage message = messageContext.getMessage();
                SOAPPart soapPart = message.getSOAPPart();
                SOAPEnvelope soapEnvelope;
                soapEnvelope = soapPart.getEnvelope();
                SOAPHeader soapHeader = soapEnvelope.getHeader();
                if (soapHeader == null) {
                    soapHeader = soapEnvelope.addHeader();
                }

                Name name;
                name = soapEnvelope.createName("entity", "security", "http://ca.essd.0403");

                SOAPElement element = soapHeader.addHeaderElement(name);
                element.addTextNode(this.entityINFO.getEntityID());

                Properties properties = SecurityData.readPropertiesFile(this.entityINFO.getPath());
                String privatePath = properties.getProperty("privateKeyPath") + this.entityINFO.getEntityID() + "private.dat";

                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                message.writeTo(byteOut);

                byte[] cipherDigest = SecurityData.makeDigitalSignature(byteOut.toByteArray(), SecurityData.readPrivateKeys(privatePath));

                String sig = SecurityData.encode64(cipherDigest);

                Name sigName = soapEnvelope.createName("signature", "security", "http://ca.essd.0403");
                SOAPElement sigElement = soapHeader.addHeaderElement(sigName);
                sigElement.addTextNode(sig);

                if (entityINFO.actualCertificate == null)
                    System.out.println("TRIANTADASDASDjadas");
                SignedCertificate certificate = entityINFO.actualCertificate;
                byte[] certBytes = SecurityData.serialize(certificate);
                Name certName = soapEnvelope.createName("certificate", "security", "http://ca.essd.0403");
                SOAPElement certElement = soapHeader.addHeaderElement(certName);
                String certString = SecurityData.encode64(certBytes);
                certElement.addTextNode(certString);

            } catch (SOAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
    }

    @Override
    public Set<QName> getHeaders() {
        // TODO Auto-generated method stub
        return null;
    }

}
