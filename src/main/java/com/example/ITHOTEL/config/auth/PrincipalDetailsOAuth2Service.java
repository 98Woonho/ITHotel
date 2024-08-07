package com.example.ITHOTEL.config.auth;

import com.example.ITHOTEL.config.auth.provider.GoogleUserInfo;
import com.example.ITHOTEL.config.auth.provider.KakaoUserInfo;
import com.example.ITHOTEL.config.auth.provider.OAuth2UserInfo;
import com.example.ITHOTEL.domain.user.dto.UserDto;
import com.example.ITHOTEL.domain.user.entity.User;
import com.example.ITHOTEL.domain.user.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PrincipalDetailsOAuth2Service extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //Attribute확인
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //OAuth Server Provider 구별
        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        if(provider!=null&&provider.equals("kakao")){
            String id = oAuth2User.getAttributes().get("id").toString();
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(id, (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account"));
            oAuth2UserInfo = kakaoUserInfo;
        }else if(provider!=null&&provider.equals("google")) {
            String id = (String) oAuth2User.getAttributes().get("sub");
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(id, oAuth2User.getAttributes());
            oAuth2UserInfo = googleUserInfo;
        }

        //Db조회
        String username = oAuth2UserInfo.getProvider()+"_"+oAuth2UserInfo.getProvider_id();
        String password = RandomStringUtils.randomAlphanumeric(16);

        Optional<User> optional =  userRepository.findById(username);
        UserDto dto = null;
        if(optional.isEmpty()){
            User user = User.builder()
                    .userid(username)
                    .password(passwordEncoder.encode(password))
                    .name(oAuth2UserInfo.getName())
                    .email(oAuth2UserInfo.getEmail())
                    .phone(oAuth2UserInfo.getPhone())
                    .role("ROLE_USER")
                    .provider(oAuth2UserInfo.getProvider())
                    .provider_id(oAuth2UserInfo.getProvider_id())
                    .build();
            userRepository.save(user);
            dto = User.entityToDto(user);
        }else {
            User user = optional.get();
            dto = User.entityToDto(user);
        }

        // PrincipalDetails생성
        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setAttributes(oAuth2UserInfo.getAttributes());
        principalDetails.setAccessToken(userRequest.getAccessToken().getTokenValue());
        principalDetails.setUserDto(dto);
        return principalDetails;
    }
}
