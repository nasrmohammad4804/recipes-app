package com.nasr.recipesproject.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private boolean success;
    private String message;
    private Object data;

    public ResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseDto(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }
}
