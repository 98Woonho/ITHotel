package com.whl.hotelService.domain.userDomain.entity;

import com.whl.hotelService.domain.userDomain.dto.UserDto;
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
    private String user_id;
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
                .user_id(user.getUser_id())
                .password(user.getPassword())
                .role(user.getRole())
                .provider(user.getProvider())
                .provider_id(user.getProvider_id())
                .build();
        return dto;
    }
}

