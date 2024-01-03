package com.whl.hotelService.config.auth.provider;

import java.util.Map;

public interface OAuth2UserInfo {
    String getId();
    String getEmail();
    String getName();
    String getPhone();
    String getProvider();
    String getProvider_id();

    Map<String,Object> getAttributes();
}
