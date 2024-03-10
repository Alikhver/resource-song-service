package com.example.cloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SongInfoNotFoundException extends ResponseStatusException {

    private static final HttpStatus status = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "The song metadata with the specified id does not exist";

    public SongInfoNotFoundException() {
        super(status, MESSAGE);
    }
}
