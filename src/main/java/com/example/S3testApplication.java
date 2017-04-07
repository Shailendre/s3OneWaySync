package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
public class S3testApplication {

    @Autowired
    private DirectoryChangeListener directoryChangeListener;

	public static void main(String[] args) {
        SpringApplication.run(S3testApplication.class, args);
    }

    @PostConstruct
    public void startSync(){
        directoryChangeListener.initDirectoryWatchEvent();
    }
}
