package com.whl.hotelService.controller;

import com.whl.hotelService.config.auth.jwt.JwtProperties;
import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.config.auth.jwt.TokenInfo;
import com.whl.hotelService.domain.dto.UserDto;
import com.whl.hotelService.domain.service.UserService;
import jakarta.persistence.Id;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "login")
    public void getLogin() {
        log.info("getLogin()");
    }

    @GetMapping(value = "join")
    public void getjoin() { log.info("getjoin()"); }

    @PostMapping("join")
    public String postjoin(@Valid UserDto dto, BindingResult bindingResult, Model model){
        log.info("postjoin() dto : " + dto);

        if(bindingResult.hasFieldErrors()){
            for(FieldError error :bindingResult.getFieldErrors()){
                log.info(error.getField() +" : " + error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "user/join";
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        boolean isJoin = userService.memberjoin(dto);
        if(isJoin)
            return "redirect:/user/login?msg=MemberJoin Success!";
        else
            return "forward:/user/join?msg=Join Failed";
    }

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("ConfirmId")
    public @ResponseBody JSONObject getConfirmId(String user_id, HttpServletResponse response){
        UserController.log.info("getConfirmId id : " + user_id);
        boolean IdValid = userService.idValid(user_id);
        JSONObject obj = new JSONObject();
        if(!Objects.equals(user_id, "") && IdValid){
            obj.put("success", true);
            obj.put("message", "사용가능한 아이디입니다.");
            TokenInfo tokenInfo = jwtTokenProvider.generateToken("IdVaild", user_id, true);
            Cookie cookie = new Cookie("EmailAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
            response.addCookie(cookie);

            return obj;
        } else if(Objects.equals(user_id, "")){
            obj.put("success", false);
            obj.put("message", "아이디를 입력하세요");

            return obj;
        }
        obj.put("success", false);
        obj.put("message", "동일한 아이디가 존재합니다.");

        return obj;
    }
}
