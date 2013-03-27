package base;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
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

import sun.misc.BASE64Decoder;

public class BasicHandler implements SOAPHandler<SOAPMessageContext> {

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
        System.out.println("Passou por aqui!");

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

                Iterator<SOAPElement> element = soapHeader.getChildElements();

                SOAPElement signature = null;
                while (element.hasNext()) {
                    signature = (SOAPElement) element.next();
                    Name name = signature.getElementName();
                    if (name.getLocalName().equals("Client")) {
                        break;
                    }
                }

                String sig = signature.getValue();
                soapHeader.removeChild(signature);

                BASE64Decoder b64e = new BASE64Decoder();
                byte[] result = b64e.decodeBuffer(sig);

                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                message.writeTo(byteOut);

                //
                // get a message digest object using the MD5 algorithm
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");

                //
                // calculate the digest and print it out
                messageDigest.update(byteOut.toByteArray());
                byte[] digest = messageDigest.digest();

                //
                // get an RSA cipher object and print the provider
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");


                System.out.println("Reading public key from file " + "/home/alex/public.txt" + " ...");
                FileInputStream fin = new FileInputStream("/home/alex/public.txt");
                byte[] pubEncoded = new byte[fin.available()];
                fin.read(pubEncoded);
                fin.close();

                X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
                KeyFactory keyFacPub = KeyFactory.getInstance("RSA");

                PublicKey pub = keyFacPub.generatePublic(pubSpec);

                System.out.println(pub);

                //
                // decrypt the ciphered digest using the public key
                cipher.init(Cipher.DECRYPT_MODE, pub);
                byte[] decipheredDigest = cipher.doFinal(result);
                System.out.println("Finish decryption: ");

                //
                // compare digests
                boolean valid = true;
                if (digest.length != decipheredDigest.length) {
                    valid = false;
                }

                for (int i = 0; i < digest.length; i++) {
                    if (digest[i] != decipheredDigest[i]) {
                        valid = false;
                    }
                }

                if (valid)
                    System.out.println("VALID SIGNATURE!");
                String text = soapHeader.getTextContent();
                System.out.println("TEXT " + text);
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
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

}
