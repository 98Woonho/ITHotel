package com.whl.hotelService.controller;


import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;
import com.whl.hotelService.domain.common.service.AdminBoardServiceImpl;
import com.whl.hotelService.domain.common.service.BoardServiceImpl;
import com.whl.hotelService.domain.common.service.CommentServiceImpl;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "board")
public class BoardController {
    @Autowired
    private BoardServiceImpl boardService;
    @Autowired
    private AdminBoardServiceImpl adminBoardService;
    @Autowired
    private CommentServiceImpl commentService;

    @GetMapping("/write")
    public String writeForm() {
        return "board/write";
    }

    //    관리자 글쓰기
    @GetMapping("/admin/adminWrite")
    public String adminWriteForm(){
        return "board/admin/adminWrite";
    }
    @PostMapping("/admin/adminWrite") // 게시판 글쓰기 로그인된 유저만 글을 쓸수 있음
    public String adminWrite(BoardWriteRequestDto boardWriteRequestDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        adminBoardService.saveBoard(boardWriteRequestDto, userDetails.getUsername());

        return "redirect:/";
    }
    @PostMapping("/write") // 게시판 글쓰기 로그인된 유저만 글을 쓸수 있음
    public String write(BoardWriteRequestDto boardWriteRequestDto, Authentication authentication, RedirectAttributes redirectAttributes) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boardService.saveBoard(boardWriteRequestDto, userDetails.getUsername());
        return "redirect:/board/list";
    }

    @GetMapping("/{id}") // 게시판 조회
    public String boardDetail(@PathVariable Long id, Model model) {
        BoardResponseDto board = boardService.boardDetail(id);
        model.addAttribute("board", board);
        model.addAttribute("id", id);

        return "board/detail";
    }

    @GetMapping("/admin/{id}") // 게시판 조회
    public String adminBoardDetail(@PathVariable Long id, Model model) {
        BoardResponseDto board = adminBoardService.boardDetail(id);
        List<CommentResponseDto> commentResponseDtos = commentService.commentList(id);
        model.addAttribute("comments", commentResponseDtos);
        model.addAttribute("board", board);
        model.addAttribute("id", id);

        return "board/admin/adminDetail";
    }
    @GetMapping("/admin/adminList") // 게시판 전체 조회 + paging 처리 + 검색처리 + 답변완료 처리
    public String adminBoardList(Model model, @PageableDefault(page = 0, size = 10, sort = "id")Pageable pageable, String keyword, String type){

        Page<BoardResponseDto> boardList = adminBoardService.boardList(pageable);
        Page<BoardResponseDto> boardSerchList = adminBoardService.searchingBoardList(keyword, type, pageable);
        Page<CommentResponseDto> commentList = adminBoardService.commentList(pageable);
        if (keyword == null){
            model.addAttribute("boardList", boardList);
            model.addAttribute("commentList", commentList);
        } else {
            model.addAttribute("boardList", boardSerchList);
            model.addAttribute("commentList", commentList);
        }
        return "board/admin/adminList";
    }


    @GetMapping("/list") // 게시판 전체 조회 + paging 처리 + 검색처리 + 답변완료 처리
    public String boardList(Model model, @PageableDefault(page = 0, size = 10, sort = "id")Pageable pageable, String keyword, String type){

        Page<BoardResponseDto> boardList = boardService.boardList(pageable);
        Page<BoardResponseDto> boardSerchList = boardService.searchingBoardList(keyword, type, pageable);
        if (keyword == null){
            model.addAttribute("boardList", boardList);
        } else {
            model.addAttribute("boardList", boardSerchList);
        }
        return "board/list";
    }

    @GetMapping("/{id}/update") // 게시판 업데이트화면 이동
    public String boardUpdateForm(@PathVariable Long id, Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //로그인된 회원을 조회해서
        BoardResponseDto board = boardService.boardDetail(id);
        if (!(board.getUsername().equals(userDetails.getUsername()))) { //확인해서 같으면 수정페이지 이동
            System.out.println("userDetails.getUsername : " + userDetails.getUsername());
            System.out.println("getUsername() : " + board.getUsername());
            return "redirect:/";
        }
        else {
            model.addAttribute("board", board);
            model.addAttribute("id", id);

            return "board/update";
        }
    }

    @GetMapping("/{id}/admin/adminUpdate") // 관리자 게시판 업데이트화면 이동
    public String adminBoardUpdateForm(@PathVariable Long id, Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //로그인된 회원을 조회해서
        BoardResponseDto board = adminBoardService.boardDetail(id);
        if (!(board.getUsername().equals(userDetails.getUsername()))) { //확인해서 같으면 수정페이지 이동
            System.out.println("userDetails.getUsername : " + userDetails.getUsername());
            System.out.println("getUsername() : " + board.getUsername());
            return "redirect:/";
        }
        else {
            model.addAttribute("board", board);
            model.addAttribute("id", id);

            return "board/admin/adminUpdate";
        }
    }

    @PostMapping("/{id}/update") // 게시글 업데이트
    public String boardUpdate(@PathVariable Long id, BoardWriteRequestDto boardWriteRequestDto) {
        boardService.boardUpdate(id, boardWriteRequestDto);

        return "redirect:/board/" + id;
    }

    @PostMapping("/{id}/admin/adminUpdate") // 관리자 게시글 업데이트
    public String adminBoardUpdate(@PathVariable Long id, BoardWriteRequestDto boardWriteRequestDto) {
        adminBoardService.boardUpdate(id, boardWriteRequestDto);

        return "redirect:/board/admin/" + id;
    }

    @GetMapping("/{id}/remove") // 게시판 삭제
    public String boardRemove(@PathVariable Long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        BoardResponseDto board = boardService.boardDetail(id);
        if (!(board.getUsername().equals(userDetails.getUsername()))) {
            System.out.println("userDetails.getUsername : " + userDetails.getUsername());
            System.out.println("getUsername() : " + board.getUsername());
            return "redirect:/";
        }
        else {
            boardService.boardRemove(id);
            return "redirect:/board/list";
        }
    }
    @GetMapping("/{id}/admin/adminRemove") // 게시판 삭제
    public String adminBoardRemove(@PathVariable Long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        BoardResponseDto board = adminBoardService.boardDetail(id);
        if (!(board.getUsername().equals(userDetails.getUsername()))) {
            System.out.println("userDetails.getUsername : " + userDetails.getUsername());
            System.out.println("getUsername() : " + board.getUsername());
            return "redirect:/";
        }
        else {
            adminBoardService.boardRemove(id);
            return "redirect:/board/adminList";
        }
    }
}