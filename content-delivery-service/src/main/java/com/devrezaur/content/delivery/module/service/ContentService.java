package com.devrezaur.content.delivery.module.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ContentService {

    private static final int RADIX = 16;

    public ArrayList<String> saveContents(MultipartFile[] contents) throws NoSuchAlgorithmException {
        ArrayList<String> hashList = new ArrayList<>();
        for (MultipartFile content : contents) {
            hashList.add(generateHash(content.getName(), content.getContentType(), content.getSize()));
        }
        return hashList;
    }

    private String generateHash(String contentName, String mimeType, long size) throws NoSuchAlgorithmException {
        String transformedName = contentName + mimeType + size + new Date().getTime();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(transformedName.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, messageDigest.digest()).toString(RADIX);
    }
}
