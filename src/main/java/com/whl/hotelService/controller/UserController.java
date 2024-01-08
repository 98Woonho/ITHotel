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
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String postjoin(UserDto dto){
        log.info("postjoin() dto : " + dto);

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
    public @ResponseBody JSONObject getConfirmId(String user_id, Model model, HttpServletResponse response){
        log.info("getConfirmId id : " + user_id);
        boolean IdValid = userService.idValid(user_id, model);
        JSONObject obj = new JSONObject();
        if(IdValid){
            obj.put("success", true);
            obj.put("message", "아이디가 중복되지 않습니다.");
            TokenInfo tokenInfo = jwtTokenProvider.generateToken("IdVaild", user_id, true);
            Cookie cookie = new Cookie("EmailAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
            response.addCookie(cookie);

            return obj;
        }
        obj.put("success", false);
        obj.put("message", "아이디가 중복됩니다.");

        return obj;
    }
}
