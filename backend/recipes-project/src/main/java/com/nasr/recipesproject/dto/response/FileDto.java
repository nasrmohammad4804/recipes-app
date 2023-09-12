package com.nasr.recipesproject.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FileDto {

    private String dataEncoded;
    private String contentType;
    private String fileName;
}
