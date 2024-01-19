package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.entity.Reservation;
import com.whl.hotelService.domain.common.repository.ReservationRepository;
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
@RequestMapping(value ="admin")
public class AdminController {
    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("reservationStatus")
    public void getReservationStatus(@RequestParam(value="region") String region,
                                     Model model) {
        model.addAttribute("region", region);
    }

    @GetMapping("inquiry")
    public void getInquiry() {}

    @GetMapping("insertHotel")
    public void getInsertHotel() {}

    @GetMapping("deleteHotel")
    public void getDeleteHotel() {}

    @GetMapping("sales")
    public void getSales(Model model) {
        List<Reservation> list = reservationRepository.findReservationsInNovember();
        model.addAttribute("list", list);
    }
}
