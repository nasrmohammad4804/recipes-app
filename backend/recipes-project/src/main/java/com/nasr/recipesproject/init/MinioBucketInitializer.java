package com.nasr.recipesproject.init;

import com.nasr.recipesproject.service.MinioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinioBucketInitializer {

    @Autowired
    private MinioService minioService;

    @PostConstruct
    public void  initializeDefaultBucket() throws Exception {

        boolean result = minioService.existsBucketName();

        if(!result)
            minioService.makeDefaultBucket();
    }
}
