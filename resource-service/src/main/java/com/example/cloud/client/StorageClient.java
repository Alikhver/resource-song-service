package com.example.cloud.client;

public interface StorageClient {

    void upload(String key, byte[] content);

    byte[] download(String key);

    void delete(String key);

}
