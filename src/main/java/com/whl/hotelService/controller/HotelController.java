package com.whl.hotelService.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value="hotel")
public class HotelController {
    @GetMapping(value="find")
    public void getFind() {
        log.info("getFind()");
    }

    @GetMapping(value="findMap")
    public void getFindMap() {
        log.info("getFindMap()");
    }
}
