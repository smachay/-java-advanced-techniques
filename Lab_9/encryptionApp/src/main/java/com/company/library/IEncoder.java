package com.company.library;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;

public interface IEncoder {
    void encrypt(String inputPath, String outputPath, KeyStore keyStore, String alias, char[] password);
    void decrypt(String inputPath, String outputPath, KeyStore keyStore, String alias, char[] password);

}
