package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.service.HotelFileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value="hotelFileInfo")
public class HotelFileInfoController {
    @Autowired
    private HotelFileInfoService hotelFileInfoService;

    @DeleteMapping("delete/{fileName}/{hotelname}")
    public ResponseEntity<String> deleteHotelFileInfo(@PathVariable("fileName") String filename, @PathVariable("hotelname") String hotelname) {
        boolean isDeleted = hotelFileInfoService.isDeleteHotelFileInfo(filename, hotelname);

        if(isDeleted) {
            return new ResponseEntity("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }
}
