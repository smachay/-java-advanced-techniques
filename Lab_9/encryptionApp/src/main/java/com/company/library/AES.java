package com.company.library;

import javax.crypto.*;
import javax.swing.*;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;

public class AES implements IEncoder{
    Cipher cipher;

    @Override
    public String toString(){
        return "AES";
    }

    @Override
    public void encrypt(String inputPath, String outputPath, KeyStore keyStore, String alias, char[] password) {
        SecretKey key = null;
        try {
            key = (SecretKey) keyStore.getKey(alias, JOptionPane.showInputDialog("Enter password").toCharArray());
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedMessageBytes = cipher.doFinal(new FileInputStream(inputPath).readAllBytes());

            FileOutputStream outputStream = new FileOutputStream(outputPath);
            outputStream.write(encryptedMessageBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | KeyStoreException |
                 UnrecoverableKeyException | IOException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        ;
    }

    @Override
    public void decrypt(String inputPath, String outputPath, KeyStore keyStore, String alias, char[] password) {
        SecretKey key = null;
        try {
            key = (SecretKey) keyStore.getKey(alias, JOptionPane.showInputDialog("Enter password").toCharArray());
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decryptedMessageBytes = cipher.doFinal(new FileInputStream(inputPath).readAllBytes());

            FileOutputStream outputStream = new FileOutputStream(outputPath);
            outputStream.write(decryptedMessageBytes);

        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
