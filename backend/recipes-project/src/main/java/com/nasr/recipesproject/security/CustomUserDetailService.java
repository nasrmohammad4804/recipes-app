package com.nasr.recipesproject.security;

import com.nasr.recipesproject.domain.User;
import com.nasr.recipesproject.exception.BusinessException;
import com.nasr.recipesproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.getUserByEmail(username);
            return new UserSecurity(user);
        }catch (BusinessException e){
            throw new UsernameNotFoundException("dont find any user");
        }
    }
}
