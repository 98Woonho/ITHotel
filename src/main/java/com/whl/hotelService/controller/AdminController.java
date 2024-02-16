package com.whl.hotelService.controller;


import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;
import com.whl.hotelService.domain.common.entity.*;
import com.whl.hotelService.domain.common.service.AdminBoardService;
import com.whl.hotelService.domain.common.service.AdminService;
import com.whl.hotelService.domain.common.service.BoardService;
import com.whl.hotelService.domain.common.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(value = "admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminBoardService adminBoardService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private CommentService commentService;


    @GetMapping("reservationStatus")
    public void getReservationStatus(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
        Page<Reservation> reservationList = adminService.getAllReservationList(pageable);
        model.addAttribute("reservationList", reservationList);
    }

    @GetMapping("inquiryList")
    public JSONObject adminBoardList(Model model,
                                     @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
                                     @RequestParam(name = "keyword", required = false) String keyword,
                                     @RequestParam(name = "type", required = false) String type) {

        model.addAttribute("type", type);

        Page<BoardResponseDto> boardList = adminBoardService.boardList(pageable);
        Page<BoardResponseDto> boardSearchList = adminBoardService.searchingBoardList(keyword, type, pageable);
        Page<CommentResponseDto> commentList = adminBoardService.commentList(pageable);
        if (keyword == null) {
            model.addAttribute("boardList", boardList);
            model.addAttribute("commentList", commentList);
        } else {
            model.addAttribute("boardList", boardSearchList);
            model.addAttribute("commentList", commentList);
        }
        JSONObject obj = new JSONObject();

        obj.put("SUCCESS", true);

        return obj;
    }

    @GetMapping("/inquiryList/{id}") // 게시판 조회
    public String adminBoardDetail(@PathVariable Long id, Model model) {
        BoardResponseDto board = adminBoardService.boardDetail(id);
        List<CommentResponseDto> commentResponseDtos = commentService.commentList(id);
        model.addAttribute("comments", commentResponseDtos);
        model.addAttribute("board", board);
        model.addAttribute("id", id);

        return "admin/inquiryListDetail";
    }

    @GetMapping("/inquiryList/{id}/adminRemove") // 게시판 삭제
    public String adminBoardRemove(@PathVariable Long id) {
        adminBoardService.boardRemove(id);
        return "redirect:/admin/inquiryList";
    }

    @GetMapping("/questionInfo/{id}/adminRemove") // 게시판 삭제
    public String questionInfoBoardRemove(@PathVariable Long id) {
        adminBoardService.boardRemove(id);
        return "redirect:/user/questionInfo?function=read";
    }

    @GetMapping("/questionWrite")
    public void adminWrite(){

    }

    @PostMapping("/questionWrite") // 관리자 게시판 글쓰기
    public String write(BoardWriteRequestDto boardWriteRequestDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boardService.saveBoard(boardWriteRequestDto, userDetails.getUsername());
        return "redirect:/board/question";
    }
    @GetMapping("/noticeWrite")
    public void noticeWrite(){

    }
    @PostMapping("/noticeWrite") // 공지 게시글 쓰기
    @ResponseBody
    public String noticeWrite(BoardWriteRequestDto boardWriteRequestDto) throws IOException {
        adminBoardService.fileAttach(boardWriteRequestDto);
        return "SUCCESS";
    }
    @GetMapping("image")
    public ResponseEntity<byte[]> getImage(@RequestParam("id") long id){
        ResponseEntity<byte[]> response;
        NoticeImage noticeImage = adminBoardService.getImage(id);
        if (noticeImage == null){
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentLength(noticeImage.getSize());
            httpHeaders.setContentType(MediaType.parseMediaType(noticeImage.getType()));
            response = new ResponseEntity<>(noticeImage.getData(), httpHeaders, HttpStatus.OK);
        }
        return response;
    }


    @PostMapping("image")
    @ResponseBody
    public String postImage(@RequestParam("upload") MultipartFile file) throws IOException{
        NoticeImage noticeImage = new NoticeImage(file);
        String result = adminBoardService.uploadImage(noticeImage);
        JSONObject responseObject = new JSONObject();
        if (result.equals("SUCCESS")){
            responseObject.put("url", "/admin/image?id=" + noticeImage.getId());
        } else {
            JSONObject messageObject = new JSONObject();
            messageObject.put("message", "알 수 없는 이유로 이미지를 업로드 하지 못하였습니다.");
            responseObject.put("error", messageObject);
        }
        return responseObject.toString();
    }

    @GetMapping("registerHotel")
    public void getRegisterHotel() {

    }

    @GetMapping("modifyHotel")
    public void getModifyHotel(@RequestParam(value = "hotelName", required = false) String hotelName, Model model) {
        Hotel hotel = new Hotel();

        HotelFileInfo hotelMainFile = adminService.getHotelMainFileInfo(hotelName, true);
        model.addAttribute("hotelMainFile", hotelMainFile);

        if (hotelName != null) {
            hotel = adminService.getHotel(hotelName);
        }

        List<String> hotelList = adminService.getHotelNameList();

        List<String> regionList = adminService.getRegionList();

        List<HotelFileInfo> hotelFileList = adminService.getHotelFileInfoList(hotelName, false);

        model.addAttribute("hotelFileList", hotelFileList);
        model.addAttribute("hotel", hotel);
        model.addAttribute("regionList", regionList);
        model.addAttribute("hotelName", hotelName);
        model.addAttribute("hotelList", hotelList);
    }

    @GetMapping("monthSales")
    public void getMonthSales(@RequestParam(value = "region", required = false) String region, @RequestParam(value = "hotelName", required = false) String hotelName, Model model) {
        if(Objects.equals(region, "total")) {
            model.addAttribute("region", "전체");
        } else {
            model.addAttribute("region", region);
        }

        if(Objects.equals(hotelName, "total")) {
            model.addAttribute("hotelName", "전체");
        } else {
            model.addAttribute("hotelName", hotelName);
        }

        List<String> regionList = adminService.getRegionList();
        model.addAttribute("regionList", regionList);

        List<Hotel> allHotelList = adminService.getAllHotelList();
        model.addAttribute("allHotelList", allHotelList);

        if (Objects.equals(region, "total") && hotelName == null) {
            List<Payment> paymentList = adminService.getAllPaymentList();
            model.addAttribute("paymentList", paymentList);

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
                } else if (month == 11) {
                    novemberSales += payment.getPaidAmount();
                } else if (month == 12) {
                    decemberSales += payment.getPaidAmount();
                }
            }

            model.addAttribute("januarySales", januarySales);
            model.addAttribute("februarySales", februarySales);
            model.addAttribute("marchSales", marchSales);
            model.addAttribute("aprilSales", aprilSales);
            model.addAttribute("maySales", maySales);
            model.addAttribute("junSales", junSales);
            model.addAttribute("julySales", julySales);
            model.addAttribute("augustSales", augustSales);
            model.addAttribute("septemberSales", septemberSales);
            model.addAttribute("octoberSales", octoberSales);
            model.addAttribute("novemberSales", novemberSales);
            model.addAttribute("decemberSales", decemberSales);
        } else if(!Objects.equals(region, "total") && Objects.equals(hotelName, "total")){
            List<Payment> paymentList = adminService.getPaymentListByRegion(region);

            model.addAttribute("paymentList", paymentList);

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
                } else if (month == 11) {
                    novemberSales += payment.getPaidAmount();
                } else if (month == 12) {
                    decemberSales += payment.getPaidAmount();
                }
            }
            model.addAttribute("januarySales", januarySales);
            model.addAttribute("februarySales", februarySales);
            model.addAttribute("marchSales", marchSales);
            model.addAttribute("aprilSales", aprilSales);
            model.addAttribute("maySales", maySales);
            model.addAttribute("junSales", junSales);
            model.addAttribute("julySales", julySales);
            model.addAttribute("augustSales", augustSales);
            model.addAttribute("septemberSales", septemberSales);
            model.addAttribute("octoberSales", octoberSales);
            model.addAttribute("novemberSales", novemberSales);
            model.addAttribute("decemberSales", decemberSales);

        } else if(!Objects.equals(region, "total") && !Objects.equals(hotelName, "total")){
            List<Payment> paymentList = adminService.getPaymentListByHotelName(hotelName);

            model.addAttribute("paymentList", paymentList);

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
                } else if (month == 11) {
                    novemberSales += payment.getPaidAmount();
                } else if (month == 12) {
                    decemberSales += payment.getPaidAmount();
                }
            }
            model.addAttribute("januarySales", januarySales);
            model.addAttribute("februarySales", februarySales);
            model.addAttribute("marchSales", marchSales);
            model.addAttribute("aprilSales", aprilSales);
            model.addAttribute("maySales", maySales);
            model.addAttribute("junSales", junSales);
            model.addAttribute("julySales", julySales);
            model.addAttribute("augustSales", augustSales);
            model.addAttribute("septemberSales", septemberSales);
            model.addAttribute("octoberSales", octoberSales);
            model.addAttribute("novemberSales", novemberSales);
            model.addAttribute("decemberSales", decemberSales);
        }
    }

    @GetMapping("registerRoom")
    public void getRegisterRoom(@RequestParam(value = "hotelName", required = false) String hotelName, Model model) {
        List<String> hotelList = adminService.getHotelNameList();

        model.addAttribute("hotelName", hotelName);
        model.addAttribute("hotelList", hotelList);
    }

    @GetMapping("modifyRoom")
    public void getModifyRoom(@RequestParam(value = "hotelName", required = false) String hotelName, @RequestParam(value = "roomKind", required = false) String roomKind, Model model) {
        List<String> roomList = adminService.getRoomKindList(hotelName);

        List<String> hotelList = adminService.getHotelNameList();

        RoomFileInfo roomMainFile = adminService.getRoomMainFileInfo(hotelName, roomKind, true);

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

    @GetMapping("hotelStatus")
    public void getHotelStatus(Model model) {

        List<Hotel> hotelList = adminService.getAllHotelList();

        model.addAttribute("hotelList", hotelList);
    }

    @GetMapping("roomStatus")
    public void getRoomStatus(@RequestParam(value = "hotelName", required = false) String hotelName, Model model) {
        model.addAttribute("hotelName", hotelName);

        List<Hotel> hotelList = adminService.getAllHotelList();
        model.addAttribute("hotelList", hotelList);

        List<Room> roomList = adminService.getRoomList(hotelName);
        model.addAttribute("roomList", roomList);
    }
}
