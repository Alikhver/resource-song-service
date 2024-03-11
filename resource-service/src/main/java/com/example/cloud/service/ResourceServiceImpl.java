package com.example.cloud.service;

import com.example.cloud.client.StorageClient;
import com.example.cloud.data.Resource;
import com.example.cloud.data.ResourceRepository;
import com.example.cloud.exception.ResourceNotFoundByIdException;
import com.example.cloud.publisher.MessagePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final StorageClient storageClient;
    private final MessagePublisher messagePublisher;
    private final ObjectMapper objectMapper;

    @Override
    public byte[] getResourceById(Long id) {
        log.info("GetResourceById invoked with param: {}", id);

        String s3contentKey = resourceRepository.findById(id)
                .orElseThrow(ResourceNotFoundByIdException::new)
                .getS3ContentKey();

        return storageClient.download(s3contentKey);
    }

    @Override
    public List<Long> deleteByIds(String idsString) throws JsonProcessingException {
        var resourcesToDelete = new ArrayList<Resource>();
        for (String s : idsString.split(",")) {
            try {
                var resource = resourceRepository.findById(Long.parseLong(s))
                        .orElseThrow(ResourceNotFoundByIdException::new);
                resourcesToDelete.add(resource);
            } catch (NumberFormatException | ResourceNotFoundByIdException ignored) {
            }
        }

        var deletedIds = new ArrayList<Long>();
        resourcesToDelete.forEach(resource -> {
                Long resourceId = resource.getId();

                //TODO add rollback conditions when other request were not successful and vice versa
                storageClient.delete(resource.getS3ContentKey());
                resourceRepository.deleteById(resourceId);
                log.info("Resource with id = {} deleted", resourceId);

                //TODO split metadata deletion and resource deletion. Call deleteMetadataByResourceId once with all ids

                deletedIds.add(resourceId);
        });

        messagePublisher.postDeleteMessage(objectMapper.writeValueAsString(deletedIds));

        return deletedIds;
    }

    @Override
    public Long createResource(InputStream data) throws IOException {
        byte[] byteArray = data.readAllBytes();

        String s3ContentKey = uploadContentToS3(byteArray);

        Resource resource = new Resource();
        resource.setS3ContentKey(s3ContentKey);

        var id = resourceRepository.save(resource).getId();
        log.info("New Resource saved: {}", resource);
        messagePublisher.postCreateMessage(id);
        return id;
    }

    private String uploadContentToS3(byte[] content) {
        String s3ContentKey = UUID.randomUUID().toString();
        storageClient.upload(s3ContentKey, content);
        log.info("Uploaded content to s3 with key: {}", s3ContentKey);
        return s3ContentKey;
    }
}
