package com.nasr.recipesproject.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Builder
@Setter
@Getter
public class MinioObjectResponseDto {

    private ByteArrayResource resource;
    private MediaType contentType;
    private long contentLength;
    private HttpHeaders headers;

}
