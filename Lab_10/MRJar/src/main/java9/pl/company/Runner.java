package pl.company;

import java.io.IOException;
import java.util.List;

public class Runner {

    public static void main(String[] args){
        System.out.println(List.of("Java 9"));
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}