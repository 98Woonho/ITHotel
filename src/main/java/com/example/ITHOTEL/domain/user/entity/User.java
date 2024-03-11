package com.example.ITHOTEL.domain.user.entity;

import com.example.ITHOTEL.domain.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="user")
public class User {
    @Id
    private String userid;          //회원 아이디
    private String password;        //비밀번호 
    private String repassword;      //비밀번호 확인
    private String name;            //이름
    private String email;           //이메일
    private String phone;           //전화번호
    private String zipcode;         //우편번호
    private String addr1;           //주소
    private String addr2;           //상세주소
    private String role;            //권한 (user, admin)

    private String provider;        //Oauth2 로그인시 구분자 (구글,카카오)
    private String provider_id;     //Oauth2 로그인시 생성되는 아이디

    public static UserDto entityToDto(User user){   // enitity를 dto로 변환
        UserDto dto = UserDto.builder()
                .userid(user.getUserid())
                .password(user.getPassword())
                .repassword(user.getRepassword())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .zipcode(user.getZipcode())
                .addr1(user.getAddr1())
                .addr2(user.getAddr2())
                .role(user.getRole())
                .provider(user.getProvider())
                .provider_id(user.getProvider_id())
                .build();
        return dto;
    }
}

