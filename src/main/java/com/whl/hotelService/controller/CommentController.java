package com.whl.hotelService.controller;

import com.whl.hotelService.domain.common.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    //    댓글 작성
    @PostMapping("/{id}/comment")
    public String writeComment(@PathVariable Long id,
                               @RequestParam("content")String content,
                               Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        commentService.writeComment(id, content, userDetails.getUsername());
        return "redirect:/admin/inquiryList/" + id;
    }

    //    답변 수정
    @ResponseBody
    @PostMapping("/admin/{id}/comment/{commentId}/update")
    public String updateComment(@PathVariable Long id,
                                @PathVariable Long commentId,
                                @RequestParam("content")String content) {
        commentService.updateComment(content, commentId);
        return "/admin/inquiryList/" + id;
    }

    //    답변 삭제
    @GetMapping("/board/{id}/comment/{commentId}/remove")
    public String deleteComment(@PathVariable Long id, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/admin/inquiryList/" + id;
    }
}