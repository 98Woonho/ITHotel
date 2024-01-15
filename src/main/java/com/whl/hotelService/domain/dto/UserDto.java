package com.whl.hotelService.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String user_id;
    private String password;
    private String role;

    private String provider;
    private String provider_id;
}
