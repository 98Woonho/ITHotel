package com.whl.hotelService.config.auth.loginHandler;

import com.whl.hotelService.config.auth.PrincipalDetails;
import com.whl.hotelService.config.auth.jwt.JwtProperties;
import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.config.auth.jwt.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

        @Autowired
        private JwtTokenProvider jwtTokenProvider;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

            //--------------------------------------
            //JWT ADD
            //--------------------------------------
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            // 쿠키 생성
            Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
            response.addCookie(cookie);
            //--------------------------------------

            Collection<? extends GrantedAuthority> collection =  authentication.getAuthorities();
            collection.forEach( (role)->{
                System.out.println("[CustomLoginSuccessHandler] onAuthenticationSuccess() role : " + role);
                String role_str =  role.getAuthority();

                try {
                    if (role_str.equals("ROLE_USER")){
                        response.sendRedirect("/user/Oauthjoin");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            });
        }
}
