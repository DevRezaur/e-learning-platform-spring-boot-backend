package com.devrezaur.content.delivery.service.service;

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

/**
 * Service class for storing contents to the file system storage.
 * <p>
 * This class provides methods for interacting with file server storage.
 *
 * @author Rezaur Rahman
 */
@Service
public class ContentService {

    @Value("${content.file-upload-path}")
    private String fileUploadPath;

    private static final int RADIX = 16;
    private static Path storageLocation;

    /**
     * Initializes the storage location for content files during bean creation.
     *
     * @throws IOException if an error occurs while creating the storage directory.
     */
    @PostConstruct
    private void init() throws IOException {
        storageLocation = Paths.get(fileUploadPath).toAbsolutePath().normalize();
        Files.createDirectories(storageLocation);
    }

    /**
     * Saves the provided content files to the file system and generates unique URLs for each content.
     *
     * @param contents an array of multipart file objects representing the content files to be saved.
     * @return list of unique URLs for the saved content files.
     * @throws Exception if the provided content list is empty or invalid.
     */
    public List<String> saveContents(MultipartFile[] contents) throws Exception {
        List<String> urlList = new ArrayList<>();
        if (!isContentListValid(contents)) {
            throw new Exception("Please provide valid contents for upload!");
        }
        for (MultipartFile content : contents) {
            String extension = StringUtils.getFilenameExtension(content.getOriginalFilename());
            String hash = generateHash(content.getName(), content.getContentType(), content.getSize());
            String fileName = hash + "." + extension;
            urlList.add(fileUploadPath + "/" + fileName);
            storeContentToFileSystem(content, fileName);
        }
        return urlList;
    }

    /**
     * Checks if the provided content list is valid and not empty.
     *
     * @param contents an array of multipart file objects.
     * @return true if the content list is valid and not empty. Otherwise, returns false.
     */
    private boolean isContentListValid(MultipartFile[] contents) {
        for (MultipartFile content : contents) {
            if (content == null || content.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a unique hash for a content file based on its name, MIME type, size, and timestamp.
     *
     * @param contentName the name of the content file.
     * @param mimeType    the MIME type of the content.
     * @param size        the size of the content.
     * @return an unique hash as a string.
     * @throws NoSuchAlgorithmException if an error occurs during hash generation.
     */
    private String generateHash(String contentName, String mimeType, long size) throws NoSuchAlgorithmException {
        String transformedName = contentName + mimeType + size + new Date().getTime() +
                ThreadLocalRandom.current().nextInt();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(transformedName.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, messageDigest.digest()).toString(RADIX);
    }

    /**
     * Stores the content file to the file system using its unique hash as the filename.
     *
     * @param content  the multipart file representing the content file.
     * @param fileName the unique filename for the content.
     * @throws IOException if an error occurs during file storage.
     */
    private void storeContentToFileSystem(MultipartFile content, String fileName) throws IOException {
        Path targetLocation = storageLocation.resolve(fileName);
        Files.copy(content.getInputStream(), targetLocation);
    }
}
