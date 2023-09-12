package com.nasr.recipesproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {


    @NotBlank(message = "نام کاربر ضروری میباشد")
    private String firstName;

    @NotBlank(message = "نام خانوادگی کاربر ضروری میباشد")
    private String lastName;

    @Email(message = "فرمت ایمیل کاربر صحیح نمیباشد")
    private String email;

    @NotBlank(message = "رمز عبور الزامی میباشد")
    @Min(value = 6)
    private String password;
}
