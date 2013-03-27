package pt.ist.anacom.shared.security;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import pt.ist.shared.SecurityData;

public class GenerateKeys {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Read properties file.
        Properties properties = SecurityData.readPropertiesFile("build.properties");
        String publicPath = properties.getProperty("publicKeyPath") + args[0] + "public.dat";
        String privatePath = properties.getProperty("privateKeyPath") + args[0] + "private.dat";

        // generate an RSA key
        System.out.println("\nStart generating RSA keys");
        KeyPair keyPair = null;
        byte[] pubKeyBytes = null;
        byte[] privKeyBytes = null;

        try {
            keyPair = SecurityData.generateKeys("RSA", 1024);
            System.out.println("Finish generating RSA keys");
            pubKeyBytes = keyPair.getPublic().getEncoded();
            privKeyBytes = keyPair.getPrivate().getEncoded();
            SecurityData.writeKeys(pubKeyBytes, privKeyBytes, publicPath, privatePath);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Chaves Assimetricas adicionadas!");
    }
}
