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

    // 호텔 찾기 view
    @GetMapping(value = "find")
    public void getFind(Model model) {
        List<String> regionList = hotelService.getDistinctRegion();
        model.addAttribute("regionList", regionList);

        List<HotelFileInfo> hotelMainFileInfoList = hotelService.getHotelMainFileInfoList(true);
        model.addAttribute("hotelMainFileInfoList", hotelMainFileInfoList);
    }

    // 호텔 찾기 지도 view
    @GetMapping(value = "findMap")
    public void getFindMap() {

    }

    // 호텔 찾기 지도에서 호텔 위치 표시하기 위한 호텔 list
    @GetMapping(value = "hotelList")
    @ResponseBody
    public List<Hotel> getHotelList() {
        List<Hotel> list = hotelService.getAllHotel();
        return list;
    }

    // 호텔 등록 시 호텔 이름 중복 확인
    @GetMapping(value = "confirmHotelName")
    @ResponseBody
    public String getConfirmHotelName(@RequestParam(value = "hotelName") String hotelName) {
        return hotelService.confirmHotelName(hotelName);
    }

    // 호텔 추가
    @PostMapping(value = "add")
    public ResponseEntity<String> postAdd(HotelDto hotelDto) throws IOException {
        boolean isAdd = hotelService.addHotel(hotelDto);

        if (isAdd) {
            return new ResponseEntity("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }

    // 호텔 수정
    @PutMapping("modify")
    public ResponseEntity<String> putModify(HotelDto hotelDto) throws IOException {
        boolean isModify = hotelService.modifyHotel(hotelDto);

        if (isModify) {
            return new ResponseEntity("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }

    // 호텔 삭제
    @DeleteMapping("delete")
    @ResponseBody
    public String deleteHotel(@RequestParam(value = "hotelName") String hotelName) {
        hotelService.deleteHotel(hotelName);

        return "SUCCESS";
    }

}