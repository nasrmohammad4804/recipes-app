package com.nasr.recipesproject.service;

import com.nasr.recipesproject.domain.User;
import com.nasr.recipesproject.dto.request.SignupRequestDto;
import com.nasr.recipesproject.dto.request.UpdateProfileRequestDto;
import com.nasr.recipesproject.dto.response.UserProfileResponseDto;

public interface UserService {

    Long signUp(SignupRequestDto request);

    User getUserByEmail(String email);


    UserProfileResponseDto getUserProfileByEmail(String email);

    UserProfileResponseDto updateProfile(UpdateProfileRequestDto request , String email) throws Exception;

    String  getAvatarByEmail(String email);
}
