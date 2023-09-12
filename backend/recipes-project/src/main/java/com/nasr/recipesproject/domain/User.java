package com.nasr.recipesproject.domain;

import com.nasr.recipesproject.base.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import static com.nasr.recipesproject.domain.User.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = TABLE_NAME)
public class User extends BaseEntity<Long> {

    public static final String TABLE_NAME="user_table";
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";

    @Column(name = FIRST_NAME)
    private String firstName;

    @Column(name = LAST_NAME)
    private String lastName;

    private String email;

    private String password;

    private String avatar;
}
