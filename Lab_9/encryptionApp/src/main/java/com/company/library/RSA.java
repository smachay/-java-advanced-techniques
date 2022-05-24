package com.company.library;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class RSA implements IEncoder{

    private Cipher cipher;

    @Override
    public void encrypt(String inputPath, String outputPath, KeyStore keyStore, String alias, char[] password) {
        try {
            RSAPublicKey publicKey = (RSAPublicKey) keyStore.getCertificate(alias).getPublicKey();

            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedMessageBytes = cipher.doFinal(new FileInputStream(inputPath).readAllBytes());

            FileOutputStream outputStream = new FileOutputStream(outputPath);
            outputStream.write(encryptedMessageBytes);

        } catch (KeyStoreException | NoSuchAlgorithmException | IOException |
                 NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void decrypt(String inputPath, String outputPath, KeyStore keyStore, String alias, char[] password) {
        RSAPrivateKey privateKey = null;
        try {
            privateKey = (RSAPrivateKey) keyStore.getKey(alias, JOptionPane.showInputDialog("Enter password").toCharArray());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decryptedMessageBytes = cipher.doFinal(new FileInputStream(inputPath).readAllBytes());

            FileOutputStream outputStream = new FileOutputStream(outputPath);
            outputStream.write(decryptedMessageBytes);

        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(){
        return "RSA";
    }
}
