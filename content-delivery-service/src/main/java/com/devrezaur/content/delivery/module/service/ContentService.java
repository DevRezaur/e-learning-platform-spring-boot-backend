package com.devrezaur.content.delivery.module.service;

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
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ContentService {

    private static final int RADIX = 16;

    @Value("${content.file-upload-path}")
    private String fileUploadPath;

    private static Path storageLocation;

    @PostConstruct
    private void init() throws IOException {
        storageLocation = Paths.get(fileUploadPath).toAbsolutePath().normalize();
        Files.createDirectories(storageLocation);
    }

    public List<String> saveContents(MultipartFile[] contents) throws Exception {
        List<String> urlList = new ArrayList<>();
        if (!isContentListValid(contents)) {
            throw new Exception("Please provide valid contents for upload!");
        }
        for (MultipartFile content : contents) {
            String extension = StringUtils.getFilenameExtension(content.getOriginalFilename());
            String hash = generateHash(content.getName(), content.getContentType(), content.getSize());
            urlList.add(fileUploadPath + "/" + hash + "." + extension);
            storeContentToFileSystem(content, hash);
        }
        return urlList;
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
        String transformedName = contentName + mimeType + size + new Date().getTime() +
                ThreadLocalRandom.current().nextInt();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(transformedName.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, messageDigest.digest()).toString(RADIX);
    }

    private void storeContentToFileSystem(MultipartFile content, String hash) throws IOException {
        Path targetLocation = storageLocation.resolve(hash);
        Files.copy(content.getInputStream(), targetLocation);
    }
}
