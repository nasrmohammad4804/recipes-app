package com.nasr.recipesproject.repository;

import com.nasr.recipesproject.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;

import java.util.Optional;
import java.util.function.Function;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);

    <T> Optional<T> findByEmail(String email,Class<T> type);

    @Query("select  u.avatar from User as u where u.email=:email")
    Optional<String> findAvatarByEmail(String email);

}
