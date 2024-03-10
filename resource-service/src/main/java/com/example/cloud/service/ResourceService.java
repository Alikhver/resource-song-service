package com.example.cloud.service;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ResourceService {
    Long createResource(InputStream data) throws TikaException, IOException, SAXException;

    byte[] getResourceById(Long id);

    List<Long> deleteByIds(String idsString);
}
