package com.example.cloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongMetadataProvidedException extends ResponseStatusException {

    private static final HttpStatus status = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "Song metadata missing validation error";
    public WrongMetadataProvidedException() {
        super(status, MESSAGE);
    }
}
