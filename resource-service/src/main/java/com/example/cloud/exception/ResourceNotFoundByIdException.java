package com.example.cloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundByIdException extends ResponseStatusException {
    public ResourceNotFoundByIdException() {
        super(HttpStatus.NOT_FOUND, "The resource with the specified id does not exist");
    }
}
