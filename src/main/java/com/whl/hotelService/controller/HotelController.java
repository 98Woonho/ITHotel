package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.Room;
import com.whl.hotelService.domain.common.repository.RoomRepository;
import com.whl.hotelService.domain.common.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.whl.hotelService.controller.days.getDaysOfWeekBetweenDates;

@Slf4j
@Controller
@RequestMapping(value = "hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping(value = "find")
    public void getFind() {
        log.info("getFind()");
    }

    @GetMapping(value = "findMap")
    public void getFindMap() {
        log.info("getFindMap()");
    }

    @GetMapping(value = "info")
    public void getInfo() {
        log.info("getInfo()");
    }

    @GetMapping(value = "reservationStep1")
    public void getReservationStep1(@RequestParam(value = "hotel") String hotel,
                                    @RequestParam(value = "checkin")
                                    String checkin,
                                    @RequestParam(value = "checkout")
                                    String checkout,
                                    @RequestParam(value = "adultCount") int adultCount,
                                    @RequestParam(value = "childCount") int childCount,
                                    Model model) {

        List<Room> roomList = roomRepository.findByHotelHotelname(hotel);
        model.addAttribute("roomList", roomList);

        List<Hotel> hotelList = hotelService.getHotel();
        model.addAttribute("hotelList", hotelList);

        List<String> region = hotelService.getRegion();
        model.addAttribute("region", region);

        model.addAttribute("hotel", hotel);
        model.addAttribute("checkin", checkin);
        model.addAttribute("checkout", checkout);
        model.addAttribute("adultCount", adultCount);
        model.addAttribute("childCount", childCount);


        // 주중, 주말 가격 계산을 위한 요일 카운트 리스트 생성 코드
        // [금, 토, 주중]
        LocalDate startDate = LocalDate.parse(checkin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(checkout, DateTimeFormatter.ofPattern("yyyy-MM-dd")).minusDays(1);

        DayOfWeek[] daysOfWeekBetweenDates = getDaysOfWeekBetweenDates(startDate, endDate);

        long fridayCount = Arrays.stream(daysOfWeekBetweenDates)
                .filter(dayOfWeek -> dayOfWeek == DayOfWeek.FRIDAY)
                .count();
        long saturdayCount = Arrays.stream(daysOfWeekBetweenDates)
                .filter(dayOfWeek -> dayOfWeek == DayOfWeek.SATURDAY)
                .count();
        long weekdayCount = Arrays.stream(daysOfWeekBetweenDates)
                .filter(dayOfWeek -> dayOfWeek != DayOfWeek.FRIDAY && dayOfWeek != DayOfWeek.SATURDAY)
                .count();

        List<Long> dayCountList = new ArrayList<>();

        dayCountList.add(fridayCount);
        dayCountList.add(saturdayCount);
        dayCountList.add(weekdayCount);

        model.addAttribute("dayLength", daysOfWeekBetweenDates.length); // n박
        model.addAttribute("dayCountList", dayCountList);
    }

    @GetMapping(value = "reservationStep2")
    public void getReservationStep2() {
        log.info("getReservationStep2()");
    }
}

class days {
    // 주중, 주말 가격 계산을 위한 리스트 생성 메서드
    public static DayOfWeek[] getDaysOfWeekBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<DayOfWeek> daysOfWeekList = new ArrayList<>();

        // Iterate through the dates and add the day of the week to the list
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            daysOfWeekList.add(currentDate.getDayOfWeek());
            currentDate = currentDate.plusDays(1);
        }

        return daysOfWeekList.toArray(new DayOfWeek[0]);
    }
}