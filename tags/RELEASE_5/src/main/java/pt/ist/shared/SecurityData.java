package pt.ist.shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPHeader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SecurityData {

    public static final int VALIDITY = 30;

    public static String convertDateToString(DateTime date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        return formatter.print(date);
    }

    public static DateTime convertStringToDate(String str) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        return formatter.parseDateTime(str);
    }

    public static boolean checkCertificateValidity(DateTime startDate, DateTime endDate) {
        DateTime date = new DateTime();
        if (date.isBefore(startDate) && date.isAfter(endDate)) {
            return false;
        }
        return true;
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        byte[] serializable = out.toByteArray();
        os.close();
        out.close();
        return serializable;
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        Object object = is.readObject();
        in.close();
        is.close();
        return object;
    }

    public static String encode64(byte[] str) {
        BASE64Encoder b64e = new BASE64Encoder();
        String result = b64e.encodeBuffer(str);
        return result;
    }

    public static byte[] decode64(String str) throws IOException {
        BASE64Decoder b64e = new BASE64Decoder();
        byte[] result = b64e.decodeBuffer(str);
        return result;
    }

    public static KeyPair generateKeys(String algorithm, int bits) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(bits);
        KeyPair keys = keyGen.generateKeyPair();
        return keys;
    }

    public static PublicKey getPublicKey(String pubKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] key = decode64(pubKey);
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(key);
        KeyFactory keyFacPub = KeyFactory.getInstance("RSA");
        return keyFacPub.generatePublic(pubSpec);
    }

    public static PrivateKey getPrivateKey(String privKey_str) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] privKey_bytes = decode64(privKey_str);
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKey_bytes);
        PrivateKey privKey = keyFactory.generatePrivate(privSpec);
        return privKey;
    }

    /**
     * auxiliary method to calculate digest from text and cipher it
     * 
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public static byte[] makeDigitalSignature(byte[] text, PrivateKey privateKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException,
            InvalidKeySpecException,
            IOException {

        // get a message digest object using the MD5 algorithm
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        //
        // calculate the digest and print it out
        messageDigest.update(text);
        byte[] digest = messageDigest.digest();

        //
        // get an RSA cipher object and print the provider
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        //
        // encrypt the plaintext using the private key
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherDigest = cipher.doFinal(digest);

        return cipherDigest;
    }

    /**
     * auxiliary method to calculate new digest from text and compare it to the to
     * deciphered digest
     * 
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] text, PublicKey publicKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidKeySpecException,
            IOException,
            IllegalBlockSizeException,
            BadPaddingException {

        //
        // get a message digest object using the MD5 algorithm
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        //
        // calculate the digest and print it out
        messageDigest.update(text);
        byte[] digest = messageDigest.digest();

        //
        // get an RSA cipher object and print the provider
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        //
        // decrypt the ciphered digest using the public key
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decipheredDigest = cipher.doFinal(cipherDigest);

        //
        // compare digests
        if (digest.length != decipheredDigest.length) {
            return false;
        }

        for (int i = 0; i < digest.length; i++) {
            if (digest[i] != decipheredDigest[i]) {
                return false;
            }
        }
        return true;
    }

    public static void writeKeys(byte[] publicKey, byte[] privateKey, String publicKeyPath, String privateKeyPath) throws IOException {

        FileOutputStream fout = new FileOutputStream(publicKeyPath);
        fout.write(publicKey);
        fout.flush();
        fout.close();



        fout = new FileOutputStream(privateKeyPath);
        fout.write(privateKey);
        fout.flush();
        fout.close();

    }

    public static PublicKey readPublicKeys(String publicKeyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        FileInputStream fin = new FileInputStream(publicKeyPath);
      
        byte[] pubEncoded = new byte[fin.available()];
        fin.read(pubEncoded);
        fin.close();
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
        KeyFactory keyFacPub = KeyFactory.getInstance("RSA");

        PublicKey pub = keyFacPub.generatePublic(pubSpec);


        return pub;
    }

    public static PrivateKey readPrivateKeys(String privateKeyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        FileInputStream fin = new FileInputStream(privateKeyPath);
        byte[] privEncoded = new byte[fin.available()];
        fin.read(privEncoded);
        fin.close();

        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privEncoded);
        KeyFactory keyFacPriv = KeyFactory.getInstance("RSA");

        PrivateKey priv = keyFacPriv.generatePrivate(privSpec);

        return priv;
    }

    public static Properties readPropertiesFile(String filePath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(filePath));
        } catch (IOException e) {
            System.err.println("File does not exist");
        }

        return properties;
    }

    @SuppressWarnings("unchecked")
    public static SOAPElement getSOAPHeaderElement(SOAPHeader soapHeader, String elementName) {
        Iterator<SOAPElement> element = soapHeader.getChildElements();
        SOAPElement soapElement = null;
        while (element.hasNext()) {
            soapElement = element.next();
            Name name = soapElement.getElementName();
            if (name.getLocalName().equals(elementName)) {
                break;
            }
        }
        return soapElement;
    }
}
