package com.whl.hotelService.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping(value = "user")
public class MyinfoController {
    @GetMapping("myinfo")
    public void Myinfo(@RequestParam(value="information", required = false) String information,
                       @RequestParam(value="reservation", required = false) String reservation,
                       @RequestParam(value="question", required = false) String question,
                       Model model){
        model.addAttribute("information", information);
        model.addAttribute("reservation", reservation);
        model.addAttribute("question", question);
        log.info("get myinfo");
    }
}
