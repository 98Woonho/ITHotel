package com.whl.hotelService.config.auth.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{

    private String id;
    private Map<String,Object> attributes;


    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getPhone() {
        return (String)attributes.get("phone_number");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProvider_id() {
        return this.id;
    }
}
