package com.whl.hotelService.controller;


import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.HotelFileInfo;
import com.whl.hotelService.domain.common.entity.Payment;
import com.whl.hotelService.domain.common.entity.RoomFileInfo;
import com.whl.hotelService.domain.common.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public void getRegisterHotel() {
    }

    @GetMapping("modifyHotel")
    public void getModifyHotel(@RequestParam(value = "hotelName", required = false) String hotelName, Model model) {
        Hotel hotel = new Hotel();

        if (hotelName != null) {
            hotel = adminService.getHotel(hotelName);
        }

        List<String> hotelList = adminService.getHotelNameList();

        List<String> regionList = adminService.getRegionList();

        List<HotelFileInfo> hotelFileList = adminService.getHotelFileInfo(hotelName);

        model.addAttribute("fileList", hotelFileList);
        model.addAttribute("hotel", hotel);
        model.addAttribute("regionList", regionList);
        model.addAttribute("hotelName", hotelName);
        model.addAttribute("hotelList", hotelList);
    }

    @GetMapping("monthSales")
    public void getMonthSales(@RequestParam(value = "value", required = false) String value, Model model) {
        model.addAttribute("value", value);

        List<Payment> paymentList = adminService.getPaymentList();

        List<String> hotelNameList = adminService.getHotelNameList();

        model.addAttribute("hotelNameList", hotelNameList);

        int januarySales = 0;
        int februarySales = 0;
        int marchSales = 0;
        int aprilSales = 0;
        int maySales = 0;
        int junSales = 0;
        int julySales = 0;
        int augustSales = 0;
        int septemberSales = 0;
        int octoberSales = 0;
        int novemberSales = 0;
        int decemberSales = 0;

        for (Payment payment : paymentList) {
            String checkin = payment.getReservation().getCheckin();

            LocalDate startDate = LocalDate.parse(checkin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            int month = startDate.getMonthValue();

            if (month == 1) {
                januarySales += payment.getPaidAmount();
            } else if (month == 2) {
                februarySales += payment.getPaidAmount();
            } else if (month == 3) {
                marchSales += payment.getPaidAmount();
            } else if (month == 4) {
                aprilSales += payment.getPaidAmount();
            } else if (month == 5) {
                maySales += payment.getPaidAmount();
            } else if (month == 6) {
                junSales += payment.getPaidAmount();
            } else if (month == 7) {
                julySales += payment.getPaidAmount();
            } else if (month == 8) {
                augustSales += payment.getPaidAmount();
            } else if (month == 9) {
                septemberSales += payment.getPaidAmount();
            } else if (month == 10) {
                octoberSales += payment.getPaidAmount();
            } else if (month == 11){
                novemberSales += payment.getPaidAmount();
            } else if(month==12) {
                decemberSales += payment.getPaidAmount();
            }
        }

        model.addAttribute("januarySales",januarySales);
        model.addAttribute("februarySales",februarySales);
        model.addAttribute("marchSales",marchSales);
        model.addAttribute("aprilSales",aprilSales);
        model.addAttribute("maySales",maySales);
        model.addAttribute("junSales",junSales);
        model.addAttribute("julySales",julySales);
        model.addAttribute("augustSales",augustSales);
        model.addAttribute("septemberSales",septemberSales);
        model.addAttribute("octoberSales",octoberSales);
        model.addAttribute("novemberSales",novemberSales);
        model.addAttribute("decemberSales",decemberSales);
        model.addAttribute("paymentList", paymentList);
    }

    @GetMapping("regionSales")
    public void getRegionSales() {

    }

    @GetMapping("registerRoom")
    public void getRegisterRoom(@RequestParam(value = "hotelName", required = false) String hotelName, Model model) {
        List<String> hotelList = adminService.getHotelNameList();

        model.addAttribute("hotelName", hotelName);
        model.addAttribute("hotelList", hotelList);
    }

    @GetMapping("modifyRoom")
    public void getModifyRoom(@RequestParam(value = "hotelName", required = false) String hotelName, @RequestParam(value = "roomKind", required = false) String roomKind, Model model) {
        List<String> roomList = adminService.getRoomList(hotelName);

        List<String> hotelList = adminService.getHotelNameList();

        RoomFileInfo roomMainFile = adminService.getRoomMainFile(hotelName, roomKind, true);

        if (roomMainFile != null) {
            String[] checkinTime = roomMainFile.getRoom().getCheckinTime().split(":");
            String[] checkoutTime = roomMainFile.getRoom().getCheckoutTime().split(":");
            String checkinHour = checkinTime[0];
            String checkinMinute = checkinTime[1];
            String checkoutHour = checkoutTime[0];
            String checkoutMinute = checkoutTime[1];

            model.addAttribute("checkinHour", checkinHour);
            model.addAttribute("checkinMinute", checkinMinute);
            model.addAttribute("checkoutHour", checkoutHour);
            model.addAttribute("checkoutMinute", checkoutMinute);
        }


        List<RoomFileInfo> roomFileList = adminService.getRoomFileList(hotelName, roomKind, false);

        model.addAttribute("roomFileList", roomFileList);
        model.addAttribute("roomMainFile", roomMainFile);
        model.addAttribute("roomKind", roomKind);
        model.addAttribute("roomList", roomList);
        model.addAttribute("hotelName", hotelName);
        model.addAttribute("hotelList", hotelList);
    }
}
