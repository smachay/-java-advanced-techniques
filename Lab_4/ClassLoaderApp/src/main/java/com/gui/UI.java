package com.gui;

import com.class_loader.ExtendedClassLoader;
import com.processing.Processor;
import com.processing.Status;
import com.processing.StatusListener;
import com.processing.StatusListenerImp;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UI extends JFrame implements ActionListener {
    HashMap<String, String> paths = new HashMap<String, String>();
    static ExtendedClassLoader cl;
    StatusListenerImp statusListener;

    Class<?> newClass;
    Method getInfo, submitTask, getResult;
    Object classInstance;
    Class<?>[] parameterTypes;

    JTextField insertText;
    JComboBox classCB;

    JLabel insertLabel, progressBar, resultLabel,
            classLabel, chooseClassLabel, infoLabel;

    JButton findBtn, loadBtn;

    public static void main(String[] args) {
        new UI();


    }

    UI() {
        insertLabel = new JLabel("Podaj tekst do przetworzenia:");
        insertLabel.setBounds(30, 30, 170, 30);
        this.add(insertLabel);

        insertText = new JTextField();
        insertText.setBounds(210, 35, 150, 25);
        this.add(insertText);

        classLabel = new JLabel("Znajdz klase:");
        classLabel.setBounds(30, 90, 150, 25);
        this.add(classLabel);

        findBtn = new JButton("Szukaj");
        findBtn.addActionListener(this);
        findBtn.setBounds(210, 90, 150, 25);
        this.add(findBtn);

        chooseClassLabel = new JLabel("Wybierz klase:");
        chooseClassLabel.setBounds(30, 145, 150, 25);
        this.add(chooseClassLabel);

        classCB = new JComboBox<>();
        classCB.setBounds(210, 145, 150, 25);
        this.add(classCB);

        progressBar = new JLabel("0%");
        progressBar.setBounds(30, 200, 150, 25);
        this.add(progressBar);

        loadBtn = new JButton("Zaladuj klase");
        loadBtn.addActionListener(this);
        loadBtn.setBounds(210, 200, 150, 25);
        this.add(loadBtn);

        infoLabel = new JLabel("Info klasy:");
        infoLabel.setBounds(30, 255, 400, 25);
        this.add(infoLabel);

        resultLabel = new JLabel("Wynik:");
        resultLabel.setBounds(30, 310, 400, 25);
        this.add(resultLabel);

        this.setSize(600, 600);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findBtn) {
            JFileChooser fileChooser = new JFileChooser("C:\\Users\\Stefan\\Desktop\\Pulpit\\studia\\Java techniki zaawansowane\\Lab_4\\ClassLoader\\src\\main\\java\\com\\processing");

            fileChooser.setFileFilter(new FileNameExtensionFilter("Plik CLASS ", "class"));
            int res = fileChooser.showOpenDialog(null);

            if (res == JFileChooser.APPROVE_OPTION) {
                Path p = Path.of(fileChooser.getSelectedFile().getAbsolutePath());
                paths.put(String.valueOf(p.getFileName()), p.toString());
                classCB.addItem(p.getFileName());
            }
        }

        if (e.getSource() == loadBtn) {
            var path = classCB.getSelectedItem().toString();

            try {
                cl = new ExtendedClassLoader();
                statusListener = new StatusListenerImp();
                newClass = cl.loadClass(paths.get(path));
                classInstance = newClass.getDeclaredConstructor().newInstance();
                parameterTypes = new Class<?>[]{String.class, StatusListener.class};

                getInfo = newClass.getMethod("getInfo");
                submitTask = newClass.getMethod("submitTask", parameterTypes);
                getResult = newClass.getMethod("getResult");

                infoLabel.setText("Info klasy: " + (String) getInfo.invoke(classInstance));

                Object[] submitTaskArgs = {insertText.getText(), statusListener};
                submitTask.invoke(classInstance, submitTaskArgs);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> {
                    String result = null;

                    while (statusListener.getProgress() < 100) {

                        try {
                            Thread.sleep(105);
                            progressBar.setText(String.valueOf(statusListener.getProgress()) + "%");
                            result = (String) getResult.invoke(classInstance);

                        } catch (IllegalArgumentException | InterruptedException | IllegalAccessException | InvocationTargetException ex) {
                            ex.printStackTrace();
                        }

                    }
                    if (result != null) {
                        resultLabel.setText("Wynik: " + result);
                    } else {
                        resultLabel.setText("Wynik: podano zle dane");
                    }

                    System.gc();
                    executor.shutdown();
                });

            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException ex) {
                ex.printStackTrace();
            }

        }
    }

}
