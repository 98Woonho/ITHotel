package com.whl.hotelService.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotEmpty(message = "아이디를 입력하세요")
    private String userid;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;

    @NotEmpty(message = "비밀번호확인을 입력하세요")
    private String repassword;


    @NotEmpty(message = "이름을 입력하세요")
    private String name;

    @NotEmpty(message = "이메일을 입력하세요")
    private String email;

    @NotEmpty(message = "전화번호를 입력하세요")
    private String phone;

    @NotEmpty(message = "우편번호를 입력하세요")
    private String zipcode;

    @NotEmpty(message = "주소를 입력하세요")
    private String addr1;
    private String addr2;

    private String role;

    private String provider;
    private String provider_id;
}
