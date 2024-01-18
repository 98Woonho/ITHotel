package com.whl.hotelService.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping(value ="admin")
public class AdminController {
    @GetMapping("reservationStatus")
    public String getReservationStatus(@RequestParam(value="region") String region) {
        System.out.println(region);
        return "/admin/reservationStatus";
    }
}
