package pt.ist.anacom.presentationserver.handlers;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Set;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ist.anacom.presentationserver.PresentationServer;
import pt.ist.anacom.shared.data.Data;

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
            System.out.println("Inbound SOAP message:");
            return true;
        } else {

            System.out.println(this + ">\n\t handleRequest(MessageContext=" + context + ")");
            try {
                SOAPMessageContext messageContext = (SOAPMessageContext) context;
                SOAPMessage message = messageContext.getMessage();
                SOAPPart soapPart = message.getSOAPPart();
                SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
                SOAPHeader soapHeader = soapEnvelope.getHeader();
                if (soapHeader == null) {
                    soapHeader = soapEnvelope.addHeader();
                }

                KeyPair keyPair = PresentationServer.keyPair;
                PublicKey publicKey = keyPair.getPublic();
                PrivateKey privateKey = keyPair.getPrivate();

                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                message.writeTo(byteOut);

                byte[] cipherDigest = Data.makeDigitalSignature(byteOut.toByteArray(), privateKey);

                String sig = Data.encode64(cipherDigest);

                Name name = soapEnvelope.createName("client", "signature", "http://essd.0403.sig");
                SOAPElement element = soapHeader.addChildElement(name);
                element.addTextNode(sig);

                System.out.println("SIGNATURE " + sig);

                System.out.println("DONE");
            } catch (Exception e) {
                System.out.println(this + ">\n\tException caught in handleRequest:\n" + e);
                return false;
            }
            return true;
        }
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }
}
