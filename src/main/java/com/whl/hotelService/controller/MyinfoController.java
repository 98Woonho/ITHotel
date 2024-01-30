package com.whl.hotelService.controller;

import com.whl.hotelService.config.auth.PrincipalDetails;
import com.whl.hotelService.config.auth.jwt.JwtProperties;
import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.config.auth.jwt.TokenInfo;
import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;
import com.whl.hotelService.domain.common.entity.AdminBoard;
import com.whl.hotelService.domain.common.service.AdminBoardService;
import com.whl.hotelService.domain.common.service.BoardService;
import com.whl.hotelService.domain.common.service.CommentService;
import com.whl.hotelService.domain.user.dto.UserDto;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.service.MyinfoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(value = "user")
public class MyinfoController {

    @Autowired
    MyinfoService myinfoService;
    @Autowired
    BoardService boardService;
    @Autowired
    AdminBoardService adminBoardService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    CommentService commentService;

    @GetMapping("informationInfo")
    public void InformationInfo(@RequestParam(value="function", required = false) String function, Model model, Authentication authentication){
        log.info("get information");
        model.addAttribute("function", function);
        model.addAttribute("auth_msg", "회원정보 수정을 위해선 비밀번호가 필요합니다.");
        myinfoService.ReadMyinfo(authentication.getName(), model);
    }

    @PostMapping("infoAuth/{password}")
    public @ResponseBody JSONObject infoAuth(@PathVariable String password, Authentication authentication){
        log.info("get infoAuth");
        JSONObject obj = new JSONObject();
        boolean isValid = myinfoService.isValid(password, authentication);
        if(isValid) {
            obj.put("success", true);
        }
        else {
            obj.put("success", false);
            obj.put("massage", "비밀번호가 다릅니다.");
        }
        return obj;
    }

    @PostMapping("updateinfo")
    public String updateInfo(@Valid UserDto dto, BindingResult bindingResult, Authentication authentication, Model model, HttpServletRequest request, HttpServletResponse response){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String id = principalDetails.getName();
        String email = principalDetails.getUserDto().getEmail();
        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                model.addAttribute(error.getField() + "_msg", error.getDefaultMessage());
            }
            return "redirect:/user/informationInfo?function=update";
        }
        if(Objects.equals(dto.getUserid(), id)){
            TokenInfo tokenInfo = jwtTokenProvider.generateToken("IdAuth", id, true);
            Cookie cookie = new Cookie("IdAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        if(Objects.equals(dto.getEmail(), email)){
            TokenInfo tokenInfo = jwtTokenProvider.generateToken("EmailAuth", email, true);
            Cookie cookie = new Cookie("EmailAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        boolean isUpdate = myinfoService.UpdateInfo(dto, authentication, model, request, response);
        if(isUpdate)
            return "redirect:/user/informationInfo?function=read";
        else
            return "redirect:/user/informationInfo?function=update&msg=failed..";
    }

    @GetMapping("reservationInfo")
    public void ReservationInfo(@RequestParam(value="function", required = false) String function, Model model){
        log.info("get reservation");
        model.addAttribute("function", function);
    }

    @GetMapping("questionInfo")
    public void questionInfo(@RequestParam(value="function", required = false) String function, User user, Authentication authentication, Model model){
        List<String> hotelnames = boardService.searchHotelname();
        List<BoardResponseDto> boardList = adminBoardService.userBoardList(user, authentication);
        model.addAttribute("boardList", boardList);
        model.addAttribute("hotelnames", hotelnames);
        model.addAttribute("function", function);
    }
}
