package com.whl.hotelService.domain.userDomain.service;

import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.domain.userDomain.dto.UserDto;
import com.whl.hotelService.domain.userDomain.entity.User;
import com.whl.hotelService.domain.userDomain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean idValid(String id) {
        if (userRepository.existsById(id))
            return false;
        return true;
    }

    public boolean memberjoin(UserDto dto, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!idValid(dto.getUser_id())) {
            model.addAttribute("user_id", "동일한 아이디가 존재합니다.");
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
            model.addAttribute("user_id", "아이디의 중복여부확인이 필요합니다.");
            System.out.println("아이디 중복 확인필요");
            return false;

        } else if (!jwtTokenProvider.validateToken(idJwtAccessToken)) {
            model.addAttribute("user_id", "아이디의 중복여부확인 유효시간을 초과했습니다");
            System.out.println("아이디 유효시간 초과");
            return false;

        } else {
            Claims claims = jwtTokenProvider.parseClaims(idJwtAccessToken);
            Boolean isIdAuth = (Boolean) claims.get("IdAuth");
            String id = (String) claims.get("id");
            if (isIdAuth == null || !isIdAuth) {
                model.addAttribute("user_id", "아이디의 중복여부확인이 필요합니다.");
                System.out.println("아이디 중복 확인필요");
                return false;
            }
            if (!id.equals(dto.getUser_id())) {
                model.addAttribute("user_id", "아이디가 변동되었습니다.");
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
        dto.setRepassword(passwordEncoder.encode(dto.getRepassword()));

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        User user = new User();
        user.setUser_id(dto.getUser_id());
        user.setPassword(dto.getPassword());
        user.setRepassword(dto.getRepassword());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setZipcode(dto.getZipcode());
        user.setAddr1(dto.getAddr1());
        user.setAddr2(dto.getAddr2());
        user.setRole(dto.getRole());
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return userRepository.existsById(user.getUser_id());
    }

    public boolean isExists(String name, String email) {
        User user = userRepository.findByNameAndEmail(name, email);
        return userRepository.existsById(user.getUser_id());
    }

    public String sendId(String email){
        return userRepository.findByEmail(email).getUser_id();
    }
}
