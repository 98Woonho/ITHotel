package com.whl.hotelService.config.auth.logoutHandler;

import com.whl.hotelService.config.auth.PrincipalDetails;
import com.whl.hotelService.config.auth.jwt.JwtProperties;
import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class CustomLogoutHandler implements LogoutHandler {

    private RestTemplate restTemplate;

    public CustomLogoutHandler() {
        restTemplate = new RestTemplate();
    }

    //JWT
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        System.out.println("logout");

        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        persistentTokenRepository.removeUserTokens(authentication.getName());

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String provider = principalDetails.getUserDto().getProvider();

        if (provider != null && provider.equals("kakao")) {

            //AccessToken 추출
            String accessToken = principalDetails.getAccessToken();
            //Requeset URL
            String url = "https://kapi.kakao.com/v1/user/logout";
            //Request Header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            headers.add("Authorization", "Bearer " + accessToken);

            //Header + Parameter 단위 생성
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(headers);

            //Restamplate에 HttpEntity등록
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);


        } else if (provider != null && provider.equals("google")) {
            //AccessToken 추출
            String accessToken = principalDetails.getAccessToken();
            //URL
            String url = "https://accounts.google.com/o/oauth2/revoke?token=" + accessToken;
            //Rest Request
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        }


        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
    }
}


