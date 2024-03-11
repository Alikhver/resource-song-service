package com.example.cloud.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ResourceService {
    Long createResource(InputStream data) throws IOException;

    byte[] getResourceById(Long id);

    List<Long> deleteByIds(String idsString) throws JsonProcessingException;
}
