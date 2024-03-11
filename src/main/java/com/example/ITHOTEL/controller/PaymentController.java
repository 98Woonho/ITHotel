package com.example.ITHOTEL.controller;

import com.example.ITHOTEL.domain.common.dto.PaymentDto;
import com.example.ITHOTEL.domain.common.entity.Reservation;
import com.example.ITHOTEL.domain.common.entity.Room;
import com.example.ITHOTEL.domain.common.entity.RoomFileInfo;
import com.example.ITHOTEL.service.PaymentService;
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

    // 결제 창 view
    @GetMapping(value = "read")
    public void getRead(Model model) {
        Reservation reservation = paymentService.getReservationList();

        Room room = reservation.getRoom();
        Long id = room.getId();

        RoomFileInfo roomFileInfo = paymentService.getRoomsMainFileInfo(id);

        model.addAttribute("mainFile", roomFileInfo);
        model.addAttribute("reservation", reservation);
    }

    // 결제
    @PostMapping(value = "read")
    public ResponseEntity<String> postPayment(PaymentDto paymentDto) throws UnsupportedEncodingException {
        paymentDto.setAddress(URLDecoder.decode(paymentDto.getAddress(), "UTF-8"));
        paymentDto.setName(URLDecoder.decode(paymentDto.getName(), "UTF-8"));

        String addReservedRoomCount = paymentService.addReservedRoomCount(paymentDto.getReservationId());

        // 결제 요청을 보냈는데, 이미 모든 객실이 예약이 되었을 경우
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
}
