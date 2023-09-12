package com.nasr.recipesproject.controller;

import com.auth0.jwt.algorithms.Algorithm;
import com.nasr.recipesproject.dto.request.SignupRequestDto;
import com.nasr.recipesproject.dto.request.UpdateProfileRequestDto;
import com.nasr.recipesproject.dto.response.UserProfileResponseDto;
import com.nasr.recipesproject.enumeration.TokenType;
import com.nasr.recipesproject.exception.ResponseDto;
import com.nasr.recipesproject.service.UserService;
import com.nasr.recipesproject.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.nasr.recipesproject.constant.ConstantField.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.nasr.recipesproject.constant.ConstantField.getSigningKey;
import static com.nasr.recipesproject.util.TimeUtility.convertMiliSecondToSecond;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto request, HttpServletRequest servletRequest, HttpServletResponse response) {

        userService.signUp(request);

        Algorithm algorithm = Algorithm.HMAC256(getSigningKey().getBytes());
        String accessToken = JwtUtil.generateToken(request.getEmail(), servletRequest, algorithm, TokenType.ACCESS_TOKEN);
        String refreshToken = JwtUtil.generateToken(request.getEmail(), servletRequest, algorithm, TokenType.REFRESH_TOKEN);

        JwtUtil.convertTokensToCookie(new String[]{accessToken, refreshToken}, response);

        return ResponseEntity.status(CREATED).body(
                new ResponseDto(true, "ok", convertMiliSecondToSecond(ACCESS_TOKEN_EXPIRATION_TIME)));
    }

    @GetMapping("/user-info")
    public ResponseEntity<ResponseDto> getUserProfileByToken(@AuthenticationPrincipal String email) {
        UserProfileResponseDto userProfile = userService.getUserProfileByEmail(email);

        return ResponseEntity.ok(
                new ResponseDto(true, "ok", userProfile)
        );
    }

    @PutMapping("/change-profile")
    public ResponseEntity<ResponseDto> changeProfile(@AuthenticationPrincipal String email,
                                                     @RequestParam @NotNull MultipartFile file,
                                                     @RequestParam @NotBlank String firstName,
                                                     @RequestParam @NotBlank String lastName) throws Exception {

        UpdateProfileRequestDto request = UpdateProfileRequestDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .file(file)
                .build();

        UserProfileResponseDto userProfileResponseDto = userService.updateProfile(request, email);

        return ResponseEntity.ok(
                new ResponseDto(true,"ok",userProfileResponseDto)
        );
    }

}
