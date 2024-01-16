package com.whl.hotelService.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
public class HomeController_Re {
    @GetMapping("/")
    public String home(){
        return "index";
    }


}
