package com.example;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

/**
 *  Created by shailendra.singh on 4/6/17.
 */
@Component
public class S3Operations {

    @Autowired
    S3ConnectionManager s3ConnectionManager;

    private AmazonS3 amazonS3;

    private String destinationBucketName;

    @PostConstruct
    public void postConstruct() {
        amazonS3 = s3ConnectionManager.getAmazonS3();
        destinationBucketName = amazonS3.listBuckets().get(1).getName();
    }

    public List<Bucket> getS3Buckets() {
        return amazonS3.listBuckets();
    }

    public void createFile(File file) {
        try {
            PutObjectResult putObjectResult = amazonS3.putObject(destinationBucketName, file.getName(), file);
        } catch (SdkClientException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void deleteFile(File file) {
        try {
            amazonS3.deleteObject(destinationBucketName, file.getName());
        } catch (SdkClientException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void modifyFile(File file) {
        deleteFile(file);
        createFile(file);
    }
}
