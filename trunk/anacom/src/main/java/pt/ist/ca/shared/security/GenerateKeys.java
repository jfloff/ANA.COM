package pt.ist.ca.shared.security;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import pt.ist.shared.SecurityData;

public class GenerateKeys {

    public static void main(String[] args) {

        // Read properties file.
        Properties properties = SecurityData.readPropertiesFile("build.properties");
        String publicPath = properties.getProperty("publicKeyCAPath") + args[0] + "public.dat";
        String privatePath = properties.getProperty("privateKeyCAPath") + args[0] + "private.dat";
        // generate an RSA key
        KeyPair keyPair = null;

        try {
            keyPair = SecurityData.generateKeys("RSA", 1024);
            byte[] pubKeyBytes = keyPair.getPublic().getEncoded();
            byte[] privKeyBytes = keyPair.getPrivate().getEncoded();
            SecurityData.writeKeys(pubKeyBytes, privKeyBytes, publicPath, privatePath);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
