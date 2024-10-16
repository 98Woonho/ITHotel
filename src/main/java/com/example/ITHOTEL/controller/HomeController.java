package com.example.ITHOTEL.controller;

import com.example.ITHOTEL.domain.common.entity.Hotel;
import com.example.ITHOTEL.domain.common.entity.HotelFileInfo;
import com.example.ITHOTEL.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping("/")
    public String home(Model model){

        List<Hotel> hotelList = homeService.getAllHotel();
        model.addAttribute("hotelList", hotelList);

        List<String> region = homeService.getDistinctRegion();
        model.addAttribute("region", region);

        List<HotelFileInfo> hotelMainFileInfoList = homeService.getHotelMainFileInfoList();
        model.addAttribute("mainFileInfoList", hotelMainFileInfoList);

        return "index";
    }

    @GetMapping("terms")
    public String getTerms() {
        return "policy/terms";
    }

    @GetMapping("privacyPolicy")
    public String getPrivacyPolicy() {
        return "policy/privacyPolicy";
    }

    @GetMapping("info")
    public String getInfo() {
        return "hotelInfo/info";
    }

    @GetMapping("history")
    public String getHistory() {
        return "hotelInfo/history";
    }

    @GetMapping("award")
    public String getAward() {
        return "hotelInfo/award";
    }
}
