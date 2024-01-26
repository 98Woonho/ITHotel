package com.whl.hotelService.domain.user.service;

import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.domain.user.dto.UserDto;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class MyinfoService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public void ReadMyinfo(String id, Model model){
        User user = userRepository.getReferenceById(id);
        UserDto dto = User.entityToDto(user);

        model.addAttribute("userid", dto.getUserid());
        model.addAttribute("name", dto.getName());
        model.addAttribute("email", dto.getEmail());
        model.addAttribute("phone", dto.getPhone());
        model.addAttribute("zipcode", dto.getZipcode());
        model.addAttribute("addr1", dto.getAddr1());
        model.addAttribute("addr2", dto.getAddr2());
    }

    public boolean isValid(String password, Authentication authentication){
        User user = userRepository.getReferenceById(authentication.getName());
        UserDto dto = User.entityToDto(user);
        return passwordEncoder.matches(password, dto.getPassword());
    }

    public boolean UpdateInfo(UserDto dto, Authentication authentication, Model model, HttpServletRequest request, HttpServletResponse response){
        if (!userRepository.existsById(dto.getUserid())) {
            model.addAttribute("userid", "동일한 아이디가 존재합니다.");
            System.out.println("동일 아이디 존재");
            return false;
        }
        Cookie[] idCookies = request.getCookies();
        String idJwtAccessToken = Arrays.stream(idCookies).filter(co -> co.getName().equals("IdAuth")).findFirst()
                .map(co -> co.getValue())
                .orElse(null);

        Cookie[] emailCookies = request.getCookies();
        String emailJwtAccessToken = Arrays.stream(emailCookies).filter(co -> co.getName().equals("EmailAuth")).findFirst()
                .map(co -> co.getValue())
                .orElse(null);

        //id jwt토큰 확인
        if(idJwtAccessToken == null){
            model.addAttribute("userid", "아이디의 중복여부확인이 필요합니다.");
            System.out.println("아이디 중복 확인필요");
            return false;

        } else if (!jwtTokenProvider.validateToken(idJwtAccessToken)) {
            model.addAttribute("userid", "아이디의 중복여부확인 유효시간을 초과했습니다");
            System.out.println("아이디 유효시간 초과");
            return false;

        } else {
            Claims claims = jwtTokenProvider.parseClaims(idJwtAccessToken);
            Boolean isIdAuth = (Boolean) claims.get("IdAuth");
            String id = (String) claims.get("id");
            if (isIdAuth == null || !isIdAuth) {
                model.addAttribute("userid", "아이디의 중복여부확인이 필요합니다.");
                System.out.println("아이디 중복 확인필요");
                return false;
            }
            if (!id.equals(dto.getUserid())) {
                model.addAttribute("userid", "아이디가 변동되었습니다.");
                System.out.println("아이디 변동됨");
                return false;
            }
        }

        //email jwt토큰 확인
        if(emailJwtAccessToken == null){
            model.addAttribute("email", "이메일 인증이 필요합니다.");
            System.out.println("이메일 인증 필요");
            return false;
        } else if (!jwtTokenProvider.validateToken(emailJwtAccessToken)) {
            model.addAttribute("email", "이메일 인증 유효시간을 초과하였거나 인증을 하지 않았습니다.");
            System.out.println("이메일 유효시간 초과");
            return false;
        } else {
            Claims claims = jwtTokenProvider.parseClaims(emailJwtAccessToken);
            Boolean isEmailAuth = (Boolean) claims.get("EmailAuth");
            String id = (String) claims.get("id");
            if (isEmailAuth == null || !isEmailAuth) {
                model.addAttribute("email", "이메일 인증이 필요합니다.");
                System.out.println("이메일 인증 필요");
                return false;
            }
            if (!id.equals(dto.getEmail())) {
                model.addAttribute("email", "이메일이 변동되었습니다..");
                System.out.println("이메일 변동됨");
                return false;
            }
        }

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        User user = userRepository.getReferenceById(authentication.getName());
        user.setUserid(dto.getUserid());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setZipcode(dto.getZipcode());
        user.setAddr1(dto.getAddr1());
        user.setAddr2(dto.getAddr2());
        userRepository.save(user);

        return userRepository.existsById(user.getUserid());
    }

}
