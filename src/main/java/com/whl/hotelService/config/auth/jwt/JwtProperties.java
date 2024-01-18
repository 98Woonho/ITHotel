package com.whl.hotelService.config.auth.jwt;

public class JwtProperties {
    public static final int EXPIRATION_TIME = 600000; // 10분
    public static final String COOKIE_NAME = "JWT-AUTHENTICATION";
    public static final String ADMIN_COOKIE_NAME = "JWT-AUTHENTICATION-ADMIN";
}
