package com.nasr.recipesproject.service;

import com.nasr.recipesproject.dto.response.FileDto;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MinioService {

    FileDto getObjectByName(String objectName) throws Exception;

    FileDto uploadObject(MultipartFile file,String objectName) throws Exception;


    boolean existsBucketName() throws Exception;

    void makeDefaultBucket() throws Exception;
}
