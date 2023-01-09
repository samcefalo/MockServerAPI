package me.samcefalo.mockserverapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class MockServerApiApplication {

    private static long STARTTIME = System.currentTimeMillis();
    private static String ID = UUID.randomUUID().toString().substring(0, 5);

    public static void main(String[] args) {
        SpringApplication.run(MockServerApiApplication.class, args);
    }

    public static long startTimeStamp(){
        return STARTTIME;
    }

    public static String id(){
        return ID;
    }

}
