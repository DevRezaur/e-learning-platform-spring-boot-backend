package com.devrezaur.content.delivery.module.service;

import com.devrezaur.content.delivery.module.model.Content;
import com.devrezaur.content.delivery.module.repository.ContentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ContentService {

    private static final int RADIX = 16;

    @Value("${content.file-upload-path}")
    private String fileUploadPath;

    private static Path storageLocation;

    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @PostConstruct
    private void init() throws IOException {
        storageLocation = Paths.get(fileUploadPath).toAbsolutePath().normalize();
        Files.createDirectories(storageLocation);
    }

    public List<UUID> saveContents(MultipartFile[] contents) throws Exception {
        List<UUID> contentIdList = new ArrayList<>();
        if (!isContentListValid(contents)) {
            throw new Exception("Please provide valid contents for upload!");
        }
        for (MultipartFile content : contents) {
            String extension = StringUtils.getFilenameExtension(content.getOriginalFilename());
            String hash = generateHash(content.getName(), content.getContentType(), content.getSize());
            contentIdList.add(saveContentInfoToDB(content.getName(), content.getContentType(), extension,
                    content.getSize(), hash));
            storeContentToFileSystem(content, hash);
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

    private UUID saveContentInfoToDB(String contentName, String mimeType, String extension, long size, String hash) {
        try {
            Content content = contentRepository.save(new Content(null, contentName, mimeType, extension, size, hash));
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

    private void storeContentToFileSystem(MultipartFile content, String hash) throws IOException {
        Path targetLocation = storageLocation.resolve(hash);
        Files.copy(content.getInputStream(), targetLocation);
    }
}
