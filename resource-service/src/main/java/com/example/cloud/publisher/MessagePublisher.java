package com.example.cloud.publisher;

public interface MessagePublisher {
    void postCreateMessage(Object message);

    void postDeleteMessage(Object message);
}
