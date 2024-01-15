package com.whl.hotelService.domain.entity;

import com.whl.hotelService.domain.dto.UserDto;
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
