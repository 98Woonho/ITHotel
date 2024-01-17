package com.whl.hotelService.config.auth;

import com.whl.hotelService.config.auth.provider.GoogleUserInfo;
import com.whl.hotelService.config.auth.provider.KakaoUserInfo;
import com.whl.hotelService.config.auth.provider.OAuth2UserInfo;
import com.whl.hotelService.domain.user.dto.UserDto;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(id, oAuth2User.getAttributes());
            oAuth2UserInfo = kakaoUserInfo;
        }else if(provider!=null&&provider.equals("google")) {
            String id = (String) oAuth2User.getAttributes().get("sub");
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(id, oAuth2User.getAttributes());
            oAuth2UserInfo = googleUserInfo;
        }

        //Db조회
        String username = oAuth2UserInfo.getProvider()+"_"+oAuth2UserInfo.getProvider_id();
        String password = passwordEncoder.encode("1234");

        Optional<User> optional =  userRepository.findById(username);
        UserDto dto = null;
        if(optional.isEmpty()){
            User user = User.builder()
                    .id(username)
                    .password(password)
                    .role("ROLE_USER")
                    .provider(oAuth2UserInfo.getProvider())
                    .provider_id(oAuth2UserInfo.getProvider_id())
                    .build();
            userRepository.save(user);
            dto = User.entityToDto(user);
        }else{
            User user = optional.get();
            dto = User.entityToDto(user);

        }

        //PrincipalDetails생성
        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setAttributes(oAuth2UserInfo.getAttributes());
        principalDetails.setAccessToken(userRequest.getAccessToken().getTokenValue());
        principalDetails.setUserDto(dto);
        return principalDetails;
    }
}
