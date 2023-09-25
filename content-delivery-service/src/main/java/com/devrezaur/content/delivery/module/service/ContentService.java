package com.devrezaur.content.delivery.module.service;

import com.devrezaur.content.delivery.module.model.Content;
import com.devrezaur.content.delivery.module.repository.ContentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ContentService {

    private static final int RADIX = 16;

    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public List<UUID> saveContents(MultipartFile[] contents) throws Exception {
        List<UUID> contentIdList = new ArrayList<>();
        if (!isContentListValid(contents)) {
            throw new Exception("Please provide valid contents for upload!");
        }
        for (MultipartFile content : contents) {
            String hash = generateHash(content.getName(), content.getContentType(), content.getSize());
            contentIdList.add(saveContentInfoToDB(content.getName(), content.getContentType(),
                    content.getSize(), hash));
        }
        return contentIdList;
    }

    private boolean isContentListValid(MultipartFile[] contents) {
        for (MultipartFile content : contents) {
            if (content == null || content.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String generateHash(String contentName, String mimeType, long size) throws NoSuchAlgorithmException {
        String transformedName = contentName + mimeType + size + new Date().getTime();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(transformedName.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, messageDigest.digest()).toString(RADIX);
    }

    private UUID saveContentInfoToDB(String contentName, String mimeType, long size, String hash) {
        try {
            Content content = contentRepository.save(new Content(null, contentName, mimeType, size, hash));
            return content.getContentId();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Content> getContentsInfo(ArrayList<UUID> contentIds) {
        List<Content> contentList = new ArrayList<>();
        for (UUID contentId : contentIds) {
            Content content = contentRepository.findByContentId(contentId);
            contentList.add(content);
        }
        return contentList;
    }
}
