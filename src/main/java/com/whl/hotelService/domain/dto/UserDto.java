package com.whl.hotelService.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotBlank(message="아이디를 입력하세요")
    private String user_id;

    @NotBlank(message="비밀번호를 입력하세요")
    private String password;

    @NotBlank(message="비밀번호확인을 입력하세요")
    private String repassword;

    @NotBlank(message="이름을 입력하세요")
    private String name;

    @NotBlank(message="이메일을 입력하세요")
    private String email;

    @NotBlank(message="우편번호를 입력하세요")
    private String zipcode;

    @NotBlank(message="주소를 입력하세요")
    private String addr1;

    private String addr2;

    private String role;

    private String provider;

    private String provider_id;
}
