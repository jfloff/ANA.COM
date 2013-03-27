package base;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.util.Set;

import javax.crypto.Cipher;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import sun.misc.BASE64Encoder;

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

                //
                // generate an RSA key pair
                System.out.println( "\nStart generating RSA key" );
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(1024);
                KeyPair keys = keyGen.generateKeyPair();
                byte[] pubEncoded = keys.getPublic().getEncoded();

                FileOutputStream fout = new FileOutputStream("/home/alex/public.txt");
                fout.write(pubEncoded);
                fout.flush();
                fout.close();

                byte[] privEncoded = keys.getPrivate().getEncoded();

                fout = new FileOutputStream("/home/alex/private.txt");
                fout.write(privEncoded);
                fout.flush();
                fout.close();

                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                message.writeTo(byteOut);

                //
                // get a message digest object using the MD5 algorithm
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");

                // calculate the digest and print it out
                messageDigest.update(byteOut.toByteArray());
                byte[] digest = messageDigest.digest();

                //
                // get an RSA cipher object and print the provider
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

                //
                // encrypt the plaintext using the private key
                cipher.init(Cipher.ENCRYPT_MODE, keys.getPrivate());
                byte[] cipherDigest = cipher.doFinal(digest);
                System.out.println("Finish encryption: ");

                BASE64Encoder b64e = new BASE64Encoder();
                String result = b64e.encodeBuffer(cipherDigest);

                Name name = soapEnvelope.createName("client", "signature", "http://essd.0403.sig");
                SOAPElement element = soapHeader.addChildElement(name);
                element.addTextNode(result);
                System.out.println("SIGNATURE " + result);

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
