package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/credentials.properties");
        ) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hello world!");
        System.out.println(properties.getProperty("password"));
    }
}