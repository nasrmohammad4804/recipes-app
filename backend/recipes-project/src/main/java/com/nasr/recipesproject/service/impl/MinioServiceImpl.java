package com.nasr.recipesproject.service.impl;

import com.nasr.recipesproject.dto.response.FileDto;
import com.nasr.recipesproject.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import okhttp3.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${spring.minio.bucket}")
    private String minioBucket;

    @Override
    public FileDto getObjectByName(String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectResponse response = minioClient.getObject(
                GetObjectArgs.builder()
                        .object(objectName)
                        .bucket(minioBucket)
                        .build()
        );

        Headers responseHeader = response.headers();
        String fileName = responseHeader.get("X-Amz-Meta-filename");
        String contentType = responseHeader.get("content-type");

        byte[] resource = response.readAllBytes();


        return FileDto.builder()
                .dataEncoded(Base64.getEncoder().encodeToString(resource))
                .fileName(fileName)
                .contentType(contentType)
                .build();

    }

    @Override
    public FileDto uploadObject(MultipartFile file, String objectName) throws Exception {

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioBucket)
                .userMetadata(Map.of("filename", Objects.requireNonNull(file.getOriginalFilename())))
                .contentType(file.getContentType())
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());

        return FileDto.builder()
                .contentType(file.getContentType())
                .fileName(file.getOriginalFilename())
                .dataEncoded(
                        Base64.getEncoder().encodeToString(file.getBytes()
                        )).build();
    }

    @Override
    public boolean existsBucketName() throws Exception {

        return minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(minioBucket)
                        .build()
        );
    }

    @Override
    public void makeDefaultBucket() throws Exception {

        minioClient.makeBucket(
                MakeBucketArgs.builder()
                        .bucket(minioBucket).build()
        );
    }
}
