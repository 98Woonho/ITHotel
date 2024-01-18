package com.whl.hotelService.controller;

import com.whl.hotelService.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Authentication authentication,
                       Model model){
        System.out.println(authentication);
        model.addAttribute("authentication", authentication);
        return "index";
    }


}
