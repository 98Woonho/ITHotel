package com.whl.hotelService.controller;

import com.whl.hotelService.config.auth.PrincipalDetails;
import com.whl.hotelService.config.auth.jwt.JwtProperties;
import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.config.auth.jwt.TokenInfo;
import com.whl.hotelService.domain.user.dto.UserDto;
import com.whl.hotelService.domain.user.service.MyinfoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(value = "user")
public class MyinfoController {

    @Autowired
    MyinfoService myinfoService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("informationInfo")
    public void getInformationInfo(@RequestParam(value="function", defaultValue = "read") String function,
                                   @RequestParam(value="userid_msg", required = false) String userid_msg,
                                   @RequestParam(value="name_msg", required = false) String name_msg,
                                   @RequestParam(value="email_msg", required = false) String email_msg,
                                   @RequestParam(value="phone_msg", required = false) String phone_msg,
                                   @RequestParam(value="zipcode_msg", required = false) String zipcode_msg,
                                   @RequestParam(value="addr1_msg", required = false) String addr1_msg,
                                   @RequestParam(value="msg", required = false) String msg,
                                Model model, Authentication authentication, HttpServletRequest request){
        log.info("get information");
        model.addAttribute("auth_msg", "회원정보 수정을 위해 비밀번호가 필요합니다.");

        model.addAttribute("function", function);
        model.addAttribute("userid_msg", userid_msg);
        model.addAttribute("name_msg", name_msg);
        model.addAttribute("email_msg", email_msg);
        model.addAttribute("phone_msg", phone_msg);
        model.addAttribute("zipcode_msg", zipcode_msg);
        model.addAttribute("addr1_msg", addr1_msg);
        model.addAttribute("msg", msg);
        myinfoService.ReadMyinfo(authentication.getName(), model);
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(Objects.equals(cookie.getName(), "InfoAuth") && Objects.equals(function, "update"))
                model.addAttribute("auth", true);
        }
    }

    @PostMapping("infoAuth/{password}")
    public @ResponseBody JSONObject infoAuth(@PathVariable String password, Authentication authentication, HttpServletResponse response) throws IOException {
        log.info("get infoAuth");
        JSONObject obj = new JSONObject();
        boolean isValid = myinfoService.isValid(password, authentication);
        if(isValid) {
            TokenInfo tokenInfo = jwtTokenProvider.generateToken("InfoAuth", password, true);
            Cookie cookie = new Cookie("InfoAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME);
            cookie.setPath("/user");
            response.addCookie(cookie);
            obj.put("success", true);
        }
        else {
            obj.put("success", false);
            obj.put("massage", "비밀번호가 다릅니다.");
        }
        return obj;
    }

    @PostMapping("updateinfo")
    public String updateInfo(@Valid UserDto dto, BindingResult bindingResult, Authentication authentication,
                             RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                redirectAttributes.addAttribute(error.getField() + "_msg", error.getDefaultMessage());
            }
            return "redirect:/user/informationInfo?function=update";
        }
        boolean isUpdate = myinfoService.UpdateInfo(dto, authentication, redirectAttributes, request);
        if(isUpdate) {
            myinfoService.IdUpdate(dto);
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies){
                cookie.setPath("/user");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            return "redirect:/logout";
        }
        else
            return "redirect:/user/informationInfo?function=update";
    }

    @PostMapping("deleteinfo")
    public String deleteInfo(String password, String word, Authentication authentication, RedirectAttributes redirectAttributes){
        log.info("post deleteinfo");
        boolean isDelete = myinfoService.DeleteInfo(password, word, authentication, redirectAttributes);
        if(isDelete) {
            return "redirect:/logout";
        }else
            return "redirect:/user/informationInfo?function=delete";
    }

    @GetMapping("reservationInfo")
    public void ReservationInfo(@RequestParam(value="function", defaultValue = "read") String function, Model model){
        log.info("get reservation");
        model.addAttribute("function", function);
    }

    @GetMapping("questionInfo")
    public void questionInfo(@RequestParam(value="function", defaultValue = "create") String function, Model model){
        log.info("get question");
        model.addAttribute("function", function);
    }
}
