package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.dto.CommentRequestDto;
import com.whl.hotelService.domain.common.service.CommentService;
import com.whl.hotelService.domain.common.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentController {
    @Autowired
    private CommentServiceImpl commentService;

    //    댓글 작성
    @PostMapping("/board/{id}/comment")
    public String writeComment(@PathVariable Long id, CommentRequestDto commentRequestDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        commentService.writeComment(commentRequestDto, id, userDetails.getUsername());

        return "redirect:/board/" + id;
    }

    //    댓글 수정
    @ResponseBody
    @PostMapping("/board/{id}/comment/{commentId}/update")
    public String updateComment(@PathVariable Long id, @PathVariable Long commentId, CommentRequestDto commentRequestDto) {
        commentService.updateComment(commentRequestDto, commentId);
        return "/board/" + id;
    }

    //    댓글 삭제
    @GetMapping("/board/{id}/comment/{commentId}/remove")
    public String deleteComment(@PathVariable Long id, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/board/" + id;
    }
}