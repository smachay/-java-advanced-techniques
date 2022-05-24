package com.company.gui;

import com.company.library.AES;
import com.company.library.IEncoder;
import com.company.library.RSA;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

public class MainWindow {
    private String DIRECTORY_PATH = "C:\\Users\\Stefan\\Documents\\Encryption";
    private String keystorePath;
    private JPasswordField passwordInput;
    private JButton selectKeystoreBtn;
    private JComboBox keysComboBox;
    private JComboBox algorythmComboBox;
    private JButton selectInputBtn;
    private JButton selectOutputBtn;
    private JPanel contentPanel;
    private JLabel keystoreLabel;
    private JLabel inputFileLabel;
    private JLabel outputFileLabel;
    private JButton encryptBtn;
    private JButton decryptBtn;
    private KeyStore keyStore;
    private String inputPath;
    private String outputPath;
    private char[] password;

    //adds value to combo boxes
    public void CmbBox() {
        algorythmComboBox.addItem(new RSA());
        algorythmComboBox.addItem(new AES());
    }

    //if a file was selected function returns its path value
    public String getPath(String directoryPath){
        JFileChooser fileChooser = new JFileChooser(directoryPath);
        int res = fileChooser.showOpenDialog(null);

        if(res == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getAbsolutePath();
        else
            return "";
    }

    public MainWindow() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        CmbBox();

        selectKeystoreBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String path;
                if((path = getPath(DIRECTORY_PATH))!=""){
                    keystorePath = path;
                    keystoreLabel.setText("Keystore: "+ keystorePath);
                    keysComboBox.removeAll();
                    try {
                        password = passwordInput.getPassword();
                        keyStore = KeyStore.getInstance("JKS");
                        keyStore.load(new FileInputStream(keystorePath), password);
                        keyStore.aliases().asIterator().forEachRemaining(key-> {
                            keysComboBox.addItem(key);
                        });
                    } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });

        selectInputBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String path;
                if((path = getPath(DIRECTORY_PATH))!=""){
                    inputFileLabel.setText("Input file: "+ path);
                    inputPath = path;
                }
            }
        });

        selectOutputBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String path;
                if((path = getPath(DIRECTORY_PATH))!=""){
                    outputFileLabel.setText("Input file: "+ path);
                    outputPath = path;
                }
            }
        });


        encryptBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                IEncoder encoder = (IEncoder) algorythmComboBox.getSelectedItem();
                encoder.encrypt(inputPath, outputPath, keyStore, (String) keysComboBox.getSelectedItem(), password);
            }
        });

        decryptBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                IEncoder encoder = (IEncoder) algorythmComboBox.getSelectedItem();
                encoder.decrypt(inputPath, outputPath, keyStore, (String) keysComboBox.getSelectedItem(), password);
            }
        });
    }
    public static int i = 3;

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        JFrame frame = new JFrame();

        frame.setContentPane(new MainWindow().contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
    }


}
