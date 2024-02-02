package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.dto.HotelDto;
import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.HotelFileInfo;
import com.whl.hotelService.domain.common.repository.HotelRepository;
import com.whl.hotelService.domain.common.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping(value = "find")
    public void getFind(Model model) {
        List<String> regionList = hotelService.getDistinctRegion();
        model.addAttribute("regionList", regionList);

        List<HotelFileInfo> hotelMainFileInfoList = hotelService.getHotelMainFileInfoList(true);
        model.addAttribute("hotelMainFileInfoList", hotelMainFileInfoList);
    }

    @GetMapping(value = "findMap")
    public void getFindMap() {
        log.info("getFindMap()");
    }

    @GetMapping(value = "info")
    public void getInfo() {
        log.info("getInfo()");
    }


    @GetMapping(value = "HotelList")
    public @ResponseBody List<Hotel> getHotelInfo() {
        List<Hotel> list = hotelService.getAllHotel();
        return list;
    }


    @GetMapping(value = "confirmHotelName")
    @ResponseBody
    public String getConfirmHotelName(@RequestParam(value = "hotelName") String hotelName) {
        return hotelService.confirmHotelName(hotelName);
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

    @PutMapping("modify")
    public ResponseEntity<String> putModify(HotelDto hotelDto) throws IOException {
        boolean isModify = hotelService.modifyHotel(hotelDto);

        if (isModify) {
            return new ResponseEntity("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }

    @DeleteMapping("delete")
    @ResponseBody
    public String deleteHotel(@RequestParam(value = "hotelName") String hotelName) {
        hotelService.deleteHotel(hotelName);

        return "SUCCESS";
    }
}