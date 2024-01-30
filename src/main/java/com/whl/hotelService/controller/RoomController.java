package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.dto.RoomDto;
import com.whl.hotelService.domain.common.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("confirmKind")
    @ResponseBody
    public String getConfirmKind(@RequestParam(value = "kind") String kind,
                                 @RequestParam(value = "hotelName") String hotelName) {
        return roomService.confirmKind(kind, hotelName);
    }

    @PostMapping("add")
    public ResponseEntity<String> postAdd(RoomDto roomDto) throws IOException {
        boolean isAdd = roomService.addRoom(roomDto);

        if (isAdd) {
            return new ResponseEntity("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping("modify")
    public ResponseEntity<String> putModify(RoomDto roomDto) throws IOException {
        boolean isModify = roomService.modifyRoom(roomDto);

        if (isModify) {
            return new ResponseEntity("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }
}
