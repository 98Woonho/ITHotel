package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.dto.PaymentDto;
import com.whl.hotelService.domain.common.entity.Reservation;
import com.whl.hotelService.domain.common.entity.Room;
import com.whl.hotelService.domain.common.entity.RoomFileInfo;
import com.whl.hotelService.domain.common.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(value = "payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "read")
    public void getReservationStep2(Model model) {
        Reservation reservation = paymentService.getReservationList();

        Room room = reservation.getRoom();
        Long id = room.getId();

        RoomFileInfo roomFileInfo = paymentService.getRoomsMainFileInfo(id);

        model.addAttribute("mainFile", roomFileInfo);
        model.addAttribute("reservation", reservation);
    }


    @PostMapping(value = "read")
    public ResponseEntity<String> getPayment(PaymentDto paymentDto) throws UnsupportedEncodingException {
        paymentDto.setAddress(URLDecoder.decode(paymentDto.getAddress(), "UTF-8"));
        paymentDto.setName(URLDecoder.decode(paymentDto.getName(), "UTF-8"));

        String addReservedRoomCount = paymentService.addReservedRoomCount(paymentDto.getReservationId());
        if (Objects.equals(addReservedRoomCount, "FAILURE_NOVACANCY")) {
            return new ResponseEntity<>("FAILURE_NOVACANCY", HttpStatus.CONFLICT);
        }

        boolean isAdded = paymentService.addPayment(paymentDto);

        if(isAdded && (Objects.equals(addReservedRoomCount, "SUCCESS"))) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAILURE", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value="delete/{reservationId}")
    public ResponseEntity<String> deletePayment(@PathVariable("reservationId") String reservationId) {
        boolean isDeleted = paymentService.isDeleteReservation(reservationId);

        if(isDeleted) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAILURE", HttpStatus.CONFLICT);
        }
    }
}