package pl.company;

import java.io.IOException;
import java.util.Arrays;

public class Runner {
    public static void main(String[] args) {
        System.out.println(Arrays.asList("Java 8"));
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}