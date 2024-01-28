package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.dto.HotelDto;
import com.whl.hotelService.domain.common.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping(value = "hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

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

    @PostMapping(value = "add")
    public ResponseEntity<String> postAdd(HotelDto hotelDto) throws IOException {
        boolean isAdd = hotelService.addHotel(hotelDto);

        if (isAdd) {
            return new ResponseEntity("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping("revise")
    public ResponseEntity<String> putRevise(HotelDto hotelDto) throws IOException {
        boolean isRevise = hotelService.reviseHotel(hotelDto);

        if (isRevise) {
            return new ResponseEntity("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }
}