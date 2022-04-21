package com.company;

import javax.swing.*;
import java.awt.*;

public class User {
    private String info;
    private ImageIcon img;
    User(){
        this.info = " ";
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ImageIcon getImg() {
        return img;
    }

    public void setImg(ImageIcon img) {
        this.img = img;
    }
}
