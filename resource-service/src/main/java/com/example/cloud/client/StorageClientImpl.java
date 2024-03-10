package com.example.cloud.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class StorageClientImpl implements StorageClient {

    @Value("${my.client.s3.bucket}")
    private String bucket;
    private final S3Client s3Client;

    @Override
    public void upload(String key, byte[] content) {
        if (!bucketExists(bucket)) {
            createBucket(bucket);
        }

        PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).build();
        s3Client.putObject(request, RequestBody.fromBytes(content));
        log.info("Uploaded to S3: {}, length: {}", key, content.length);
    }

    @Override
    @SneakyThrows(IOException.class)
    public byte[] download(String key) {
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucket).key(key).build();
        byte[] content = s3Client.getObject(request).readAllBytes();
        log.info("Downloaded from S3: {}, length: {}", key, content.length);
        return content;
    }

    @Override
    public void delete(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucket).key(key).build();
        s3Client.deleteObject(request);
        log.info("Deleted from S3: {}", key);
    }

    private boolean bucketExists(String bucketName) {
        HeadBucketRequest request = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();

        try {
            s3Client.headBucket(request);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }

    private void createBucket(String bucketName) {
        log.info("Creating S3 bucket with name={}", bucketName);
        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName).build();

        s3Client.createBucket(createBucketRequest);

        S3Waiter s3Waiter = s3Client.waiter();
        HeadBucketRequest bucketRequestWaiter = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();

        WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWaiter);
        waiterResponse.matched().response().map(Object::toString).ifPresent(log::info);
        log.info("Created S3 bucket with name: {}", bucketName);
    }
}
