package com.nasr.recipesproject.service.impl;

import com.nasr.recipesproject.domain.User;
import com.nasr.recipesproject.dto.request.SignupRequestDto;
import com.nasr.recipesproject.dto.request.UpdateProfileRequestDto;
import com.nasr.recipesproject.dto.response.FileDto;
import com.nasr.recipesproject.dto.response.UserProfileResponseDto;
import com.nasr.recipesproject.exception.BusinessException;
import com.nasr.recipesproject.repository.UserRepository;
import com.nasr.recipesproject.service.MinioService;
import com.nasr.recipesproject.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.http.HttpStatus.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MinioService minioService;

    @Override
    @Transactional
    public Long signUp(SignupRequestDto request) {

        boolean result = repository.existsByEmail(request.getEmail());

        if(result)
            throw  new BusinessException("این نام کاربری قبلا در سیستم موجود میباشد", BAD_REQUEST);


        String password = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(password)
                .build();

        User userEntity = repository.save(user);
        return  userEntity.getId();
    }

    @Override
    public User getUserByEmail(String email) {
        return repository.findByEmail(email,User.class)
                .orElseThrow(BusinessException::new);
    }

    @Override
    public UserProfileResponseDto getUserProfileByEmail(String email) {
        User user = getUserByEmail(email);

        AtomicReference<FileDto> fileDto=new AtomicReference<>();

        Optional.ofNullable(user.getAvatar())
                .ifPresent(avatar -> {
                    try {
                        fileDto.set(minioService.getObjectByName(avatar));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return getUserProfile(user,fileDto.get());

    }

    @Override
    public String getAvatarByEmail(String email) {
        return repository.findAvatarByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        "dont set any avatar for this user"
                        ,
                        NOT_FOUND
                ));
    }

    @Override
    @Transactional
    public UserProfileResponseDto updateProfile(UpdateProfileRequestDto request, String email) throws Exception {

        User user = repository.findByEmail(email, User.class)
                .orElseThrow(() -> new BusinessException(
                        "dont find any user with this email"
                        ,
                        NOT_FOUND
                ));

        String objectName;

        if(StringUtils.isBlank(user.getAvatar()))
            objectName= UUID.randomUUID().toString();

        else objectName=user.getAvatar();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAvatar(objectName);

        FileDto fileDto = minioService.uploadObject(request.getFile(), objectName);

        return getUserProfile(user,fileDto);
    }

    private UserProfileResponseDto getUserProfile(User user,FileDto fileDto){

        return UserProfileResponseDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(fileDto)
                .build();
    }

}
