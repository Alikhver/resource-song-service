package org.example.cloud.service;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

public interface ProcessorService {
    public void parseAndCreateMetadata(Long resourceId) throws TikaException, IOException, SAXException;
}
