package com.example;


import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 *  Created by shailendra.singh on 4/6/17.
 */
@Component
public class S3ConnectionManager {

    private AmazonS3 amazonS3;

    @Value("${cloud.aws.region.static}")
    private String region;

    public void initS3Connection() {
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.fromName(region))
                .build();
    }

    public AmazonS3 getAmazonS3() {
        initS3Connection();
        return amazonS3;
    }

    public void setAmazonS3(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

}
