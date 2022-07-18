package com.example.scdemo;

import javax.annotation.Resource;

import com.example.scdemo.storageservice.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScdemoApplication implements CommandLineRunner {
    @Resource
    FileStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(ScdemoApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        // Init storage service (create folder) after start of app.
        storageService.init();
    }
}
