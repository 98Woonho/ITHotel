package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.HotelFileInfo;
import com.whl.hotelService.domain.common.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("reservationStatus")
    public void getReservationStatus(@RequestParam(value = "region") String region,
                                     Model model) {
        model.addAttribute("region", region);
    }

    @GetMapping("inquiry")
    public void getInquiry() {
    }

    @GetMapping("registerHotel")
    public void getRegisterHotel(Model model) {
        List<String> regionList = adminService.getRegionList();

        model.addAttribute("regionList", regionList);
    }

    @GetMapping("modifyHotel")
    public void getModifyHotel(@RequestParam(value = "hotelname", required = false) String hotelname, Model model) {
        Hotel hotel = new Hotel();

        if(hotelname != null) {
            hotel = adminService.getHotel(hotelname);
        }

        List<String> hotelList = adminService.getHotelList();

        List<String> regionList = adminService.getRegionList();

        List<HotelFileInfo> hotelFileList = adminService.getHotelFileInfo(hotelname);

        model.addAttribute("fileList", hotelFileList);
        model.addAttribute("hotel", hotel);
        model.addAttribute("regionList", regionList);
        model.addAttribute("hotelname", hotelname);
        model.addAttribute("hotelList", hotelList);
    }

    @GetMapping("sales")
    public void getSales() {
    }

    @GetMapping("registerRoom")
    public void getRegisterRoom(@RequestParam(value = "hotelname", required = false) String hotelname, Model model) {
        List<String> hotelList = adminService.getHotelList();

        model.addAttribute("hotelname", hotelname);
        model.addAttribute("hotelList", hotelList);
    }

    @GetMapping("modifyRoom")
    public void getModifyRoom() {

    }
}
