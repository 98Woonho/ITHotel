package com.whl.hotelService.controller;

import com.whl.hotelService.domain.dto.UserDto;
import com.whl.hotelService.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
