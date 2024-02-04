package com.whl.hotelService.controller;


import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.dto.HotelDto;
import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.service.AdminBoardService;
import com.whl.hotelService.domain.common.service.BoardService;
import com.whl.hotelService.domain.common.service.CommentService;
import com.whl.hotelService.domain.common.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "board")
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private AdminBoardService adminBoardService;
    @Autowired
    private HotelService hotelService;

    @GetMapping("/inquiryForm")
    public void inquiryForm(Model model) {
        List<String> hotelnames = boardService.searchHotelname();
        model.addAttribute("hotelnames", hotelnames);
    }

    @PostMapping("/inquiryForm") // 게시판 글쓰기 로그인된 유저만 글을 쓸수 있음
    public String inquiry(@RequestParam String hotelname,
                             @RequestParam String relation,
                             BoardWriteRequestDto boardWriteRequestDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        adminBoardService.saveBoard(hotelname, relation, boardWriteRequestDto, userDetails.getUsername());
        System.out.println("relation" + relation);
        return "redirect:/board/inquiryForm";
    }

    @GetMapping("/question/{id}") // 자주하는 질문 조회 수정
    public String boardDetail(@PathVariable Long id, Model model) {
        BoardResponseDto board = boardService.boardDetail(id);
        model.addAttribute("board", board);
        model.addAttribute("id", id);

        return "board/questionDetail";
    }

    @GetMapping("/question") // 게시판 전체 조회 + paging 처리 + 검색처리 + 답변완료 처리
    public void boardList(Model model, @PageableDefault(page = 0, size = 10, sort = "id") Authentication authentication, Pageable pageable, String keyword, String type) {

        Page<BoardResponseDto> boardList = boardService.boardList(pageable);
        Page<BoardResponseDto> boardSerchList = boardService.searchingBoardList(keyword, type, pageable);
        if (keyword == null) {
            model.addAttribute("boardList", boardList);
        } else {
            model.addAttribute("boardList", boardSerchList);
        }
    }

    @GetMapping("/question/{id}/update") // 게시판 업데이트화면 이동
    public String boardUpdateForm(@PathVariable Long id, Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //로그인된 회원을 조회해서
        BoardResponseDto board = boardService.boardDetail(id);
        if (!(board.getUserid().equals(userDetails.getUsername()))) { //확인해서 같으면 수정페이지 이동
            System.out.println("userDetails.getUsername : " + userDetails.getUsername());
            System.out.println("getUsername() : " + board.getUsername());
            return "redirect:/";
        } else {
            model.addAttribute("board", board);
            model.addAttribute("id", id);

            return "board/questionUpdate";
        }
    }

    @PostMapping("/question/{id}/update") // 게시글 업데이트
    public String boardUpdate(@PathVariable Long id, BoardWriteRequestDto boardWriteRequestDto) {
        boardService.boardUpdate(id, boardWriteRequestDto);

        return "redirect:/board/question/{id}";
    }

    @GetMapping("/question/{id}/remove") // 게시판 삭제
    public String boardRemove(@PathVariable Long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        BoardResponseDto board = boardService.boardDetail(id);
        if (!(board.getUserid().equals(userDetails.getUsername()))) {
            System.out.println("userDetails.getUsername : " + userDetails.getUsername());
            System.out.println("getUsername() : " + board.getUsername());
            return "redirect:/";
        } else {
            boardService.boardRemove(id);
            return "redirect:/board/question";
        }
    }

    @GetMapping("/basic")
    public void boardBasic(){

    }
    @GetMapping("hotelContact")
    public void hotelContact(HotelDto hotelDto, Model model){
        List<Hotel> hotels = hotelService.hotelContact(hotelDto);
        model.addAttribute("hotels", hotels);
    }
}
