package com.nasr.recipesproject.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Setter
@Getter
public class UpdateProfileRequestDto {

    private String firstName;
    private String lastName;
    private MultipartFile file;
}
