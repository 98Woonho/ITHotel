package com.whl.hotelService.domain.user.entity;

import com.whl.hotelService.domain.user.dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String id;
    private String password;
    private String repassword;
    private String name;
    private String email;
    private String phone;
    private String zipcode;
    private String addr1;
    private String addr2;
    private String role;

    private String provider;
    private String provider_id;

    public static UserDto entityToDto(User user){
        UserDto dto = UserDto.builder()
                .id(user.getId())
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

