package pt.ist.anacom.shared.data;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class GenerateKeys {

    public static void main(String[] args) {

        // Read properties file.
        Properties properties = Data.readPropertiesFile("build.properties");
        String publicPath = properties.getProperty("publicKeyPath") + args[0] + "public.txt";
        String privatePath = properties.getProperty("privateKeyPath") + args[0] + "private.txt";

        // generate an RSA key
        System.out.println("\nStart generating RSA keys");
        KeyPair keyPair = null;

        try {
            keyPair = Data.generateKeys("RSA", 1024);
            System.out.println("Finish generating RSA keys");
            byte[] pubKeyBytes = keyPair.getPublic().getEncoded();
            byte[] privKeyBytes = keyPair.getPrivate().getEncoded();
            Data.writeKeys(pubKeyBytes, privKeyBytes, publicPath, privatePath);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Chaves Assimetricas adicionadas!");
    }
}
