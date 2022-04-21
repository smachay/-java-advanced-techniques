package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.WeakHashMap;
import java.util.stream.Stream;

public class UI extends JFrame implements ActionListener {
    JButton btn;
    WeakHashMap<String, User> users;
    DefaultListModel dataModel;
    JList userList;


    UI(){
        users = new WeakHashMap<>();
        dataModel = new DefaultListModel();
        userList = new JList<>(dataModel);

        btn = new JButton("Select folder");
        btn.addActionListener(this);
        btn.setBounds(30,30,130,30);


        userList.setBounds(30, 90, 520, 300);

        this.add(btn);
        this.add(userList);

        this.setSize(600,600);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btn){
            JFileChooser folderChooser = new JFileChooser("C:\\Users\\Stefan\\Documents\\Users");

            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int res = folderChooser.showOpenDialog(null);

            if(res == JFileChooser.APPROVE_OPTION){
                String folderPath = folderChooser.getSelectedFile().getAbsolutePath();
                try {
                    writeUserInfo(folderPath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private String getTextFromFile(String p)throws Exception{
        Path path = Paths.get(p);

        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(s -> sb.append(s).append(" "));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //returning user info
        return sb.toString();
    }
    private ImageIcon getImageFromFile(String p) throws IOException {
        File imgFile = new File(p);
        return new ImageIcon(ImageIO.read(imgFile));
    }

    private void writeUserInfo(String directory) throws Exception {
        User user = new User();
        String recordPath = directory.concat("\\record.txt");
        String imagePath = directory.concat("\\image.png");

        dataModel.clear();
        try {
            if((users.get(directory)) != null)
                System.out.println("Loaded from a list");
            else{
                String userInfo = getTextFromFile(recordPath);
                ImageIcon userImage = getImageFromFile(imagePath);
                user.setInfo(userInfo);
                user.setImg(userImage);
                users.put(directory, user);

                System.out.println("Loaded from a hard drive");
            }

            dataModel.add(0,user.getInfo());
            dataModel.add(1,user.getImg());
            //System.out.println(user.getInfo());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
