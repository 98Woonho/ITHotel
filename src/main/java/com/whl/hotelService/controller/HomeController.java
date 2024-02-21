package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.repository.HotelRepository;
import com.whl.hotelService.domain.common.service.HotelService;
import com.whl.hotelService.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private HotelService hotelService;

    @GetMapping("/")
    public String home(Model model){

        List<Hotel> hotelList = hotelService.getAllHotel();
        model.addAttribute("hotelList", hotelList);

        List<String> region = hotelService.getDistinctRegion();
        model.addAttribute("region", region);
        return "index";
    }

}
