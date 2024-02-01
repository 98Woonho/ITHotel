package com.whl.hotelService.domain.user.service;

import com.whl.hotelService.config.auth.jwt.JwtProperties;
import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.domain.user.dto.UserDto;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean idValid(String id) {
        return !userRepository.existsById(id);
    }

    public boolean memberjoin(UserDto dto, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!idValid(dto.getUserid())) {
            model.addAttribute("userid", "동일한 아이디가 존재합니다.");
            System.out.println("동일 아이디 존재");
            return false;
        }
        if (!dto.getPassword().equals(dto.getRepassword())) {
            model.addAttribute("password", "비밀번호가 일치하지 않습니다.");
            System.out.println("비밀번호 불일치");
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

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        dto.setRepassword(passwordEncoder.encode(dto.getPassword()));

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            cookie.setPath("/user");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        User user = new User();
        user.setUserid(dto.getUserid());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setZipcode(dto.getZipcode());
        user.setAddr1(dto.getAddr1());
        user.setAddr2(dto.getAddr2());
        user.setRole(dto.getRole());
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return userRepository.existsById(user.getUserid());
    }

    public boolean OauthMemberJoin(UserDto dto, HttpServletRequest request, Model model){
        Cookie[] JwtCookies = request.getCookies();
        String JwtAccessToken = Arrays.stream(JwtCookies).filter(co -> co.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                .map(co -> co.getValue())
                .orElse(null);
        if(JwtAccessToken == null){
            model.addAttribute("success", false);
            model.addAttribute("message", "JWT토큰이 존재하지 않습니다");
            return false;

        } else if(!jwtTokenProvider.validateToken(JwtAccessToken)){
            model.addAttribute("success", false);
            model.addAttribute("message", "JWT토큰이 만료되었습니다");
            return false;

        } else {
            Claims claims = jwtTokenProvider.parseClaims(JwtAccessToken);
            String id = (String) claims.get("username");
            User user = userRepository.getReferenceById(id);
            if(Objects.equals(user.getProvider(), "kakao")) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                user.setZipcode(dto.getZipcode());
                user.setAddr1(dto.getAddr1());
                user.setAddr2(dto.getAddr2());
                userRepository.save(user);

            } else if(Objects.equals(user.getProvider(), "google")){
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                user.setPhone(dto.getPhone());
                user.setZipcode(dto.getZipcode());
                user.setAddr1(dto.getAddr1());
                user.setAddr2(dto.getAddr2());
                userRepository.save(user);
            }
            return true;
        }
    }

    public boolean isExists(String name, String email) {
        User user = userRepository.findByNameAndEmail(name, email);
        return userRepository.existsById(user.getUserid());
    }

    public boolean isExists(String id, String name, String email) {
        User user = userRepository.findByNameAndEmail(name, email);
        return Objects.equals(user.getUserid(), id);
    }

    public String sendId(String email){
        return userRepository.findByEmail(email).getUserid();
    }

    public void sendpw(String email, String pw){
        User user = userRepository.findByEmail(email);
        UserDto dto = User.entityToDto(user);
        dto.setPassword(passwordEncoder.encode(pw));
        dto.setRepassword(passwordEncoder.encode(pw));
        user.setPassword(dto.getPassword());
        user.setPassword(dto.getRepassword());
        userRepository.save(user);
    }
}
