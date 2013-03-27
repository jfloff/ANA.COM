package pt.ist.anacom.applicationserver.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ist.anacom.applicationserver.ApplicationServerWebService;
import pt.ist.anacom.shared.data.Data;

public class ServerHandler implements SOAPHandler<SOAPMessageContext> {

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

            System.out.println("Inbound SOAP message:");

            try {
                SOAPMessage message = context.getMessage();
                SOAPPart soapPart = message.getSOAPPart();
                SOAPEnvelope soapEnvelope;
                soapEnvelope = soapPart.getEnvelope();
                SOAPHeader soapHeader = soapEnvelope.getHeader();
                SOAPBody soapBody = soapEnvelope.getBody();

                if (soapHeader != null) {

                    Iterator<SOAPElement> element = soapHeader.getChildElements();

                    SOAPElement signature = null;
                    while (element.hasNext()) {
                        signature = (SOAPElement) element.next();
                        Name name = signature.getElementName();
                        if (name.getLocalName().equals("client")) {
                            break;
                        }
                    }
                    if (signature != null) {
                        String sig = signature.getValue();
                        soapHeader.removeChild(signature);

                        byte[] result = Data.decode64(sig);

                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        message.writeTo(byteOut);

                        Properties prop = Data.readPropertiesFile(ApplicationServerWebService.path);
                        PublicKey publicKey = Data.readPublicKeys(prop.getProperty("publicKeyPath") + "clientpublic.txt");

                        if (Data.verifyDigitalSignature(result, byteOut.toByteArray(), publicKey))
                            System.out.println("VALID SIGNATURE");
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
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }
}
