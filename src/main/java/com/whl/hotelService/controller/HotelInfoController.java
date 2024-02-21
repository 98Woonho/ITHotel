package com.whl.hotelService.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("hotelInfo")
public class HotelInfoController {
    @GetMapping("info")
    public void getInfo() {

    }

    @GetMapping("history")
    public void getHistory() {

    }

    @GetMapping("award")
    public void getAward() {

    }
}
