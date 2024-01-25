package com.whl.hotelService.controller;

import com.whl.hotelService.domain.user.service.MyinfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping(value = "myinfo")
public class MyinfoController {

    @Autowired
    MyinfoService myinfoService;

    @GetMapping("informationInfo")
    public void InformationInfo(@RequestParam(value="function", required = false) String function){
        log.info("get information");
    }

    @GetMapping("reservationInfo")
    public void ReservationInfo(@RequestParam(value="function", required = false) String function){
        log.info("get reservation");
    }

    @GetMapping("questionInfo")
    public void questionInfo(@RequestParam(value="function", required = false) String function){
        log.info("get question");
    }
}
