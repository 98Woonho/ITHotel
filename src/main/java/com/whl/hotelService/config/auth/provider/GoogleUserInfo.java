package com.whl.hotelService.config.auth.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo{

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
        return "010-****-****";
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProvider_id() {
        return (String)attributes.get("sub");
    }
}
