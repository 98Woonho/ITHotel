package com.example.ITHOTEL.controller;

import com.example.ITHOTEL.config.auth.PrincipalDetails;
import com.example.ITHOTEL.domain.user.dto.UserDto;
import com.example.ITHOTEL.service.UserService;
import com.example.ITHOTEL.config.auth.jwt.JwtProperties;
import com.example.ITHOTEL.config.auth.jwt.JwtTokenProvider;
import com.example.ITHOTEL.config.auth.jwt.TokenInfo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String Email_code;

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(String.class, "phone", new PhoneNumberEditor());
    }

    @GetMapping(value = "login")
    public void getLogin() {
    }

    @GetMapping("existId")
    public @ResponseBody JSONObject existId(String name, String email) {
        JSONObject obj = new JSONObject();
        if (userService.isExists(name, email)) {
            obj.put("success", true);
            obj.put("message", "입력하신 이메일로 임시코드가 발송되었습니다");

            return obj;
        }
        obj.put("success", false);
        obj.put("message", "회원정보에 존재하지 않는 이메일입니다.");

        return obj;
    }

    @GetMapping("existPw")
    public @ResponseBody JSONObject existId(String id, String name, String email) {
        JSONObject obj = new JSONObject();
        if (userService.isExists(id, name, email)) {
            obj.put("success", true);
            obj.put("message", "입력하신 이메일로 임시코드가 발송되었습니다");

            return obj;
        }
        obj.put("success", false);
        obj.put("message", "회원정보에 존재하지 않는 아이디거나 이메일입니다.");

        return obj;
    }

    @GetMapping("findId")
    public void findId(){
    }

    @GetMapping("findPw")
    public void findPw(){
    }

    @GetMapping("sendId")
    public @ResponseBody JSONObject sendid(@RequestParam boolean confirm, @RequestParam String email) {
        JSONObject obj = new JSONObject();
        if (confirm) {
            obj.put("message", userService.sendId(email));
        } else {
            obj.put("message", "인증번호가 틀렸습니다.");
        }
        return obj;
    }

    @GetMapping("sendPw")
    public @ResponseBody JSONObject sendpw(@RequestParam boolean confirm, @RequestParam String email) {
        JSONObject obj = new JSONObject();
        if (confirm) {
            //랜덤 임시비밀번호 생성
            String pw = RandomStringUtils.randomAlphanumeric(16);

            //메일 메시지 만들기
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[HOTEL] 임시 비밀번호 발급");
            message.setText(pw);

            javaMailSender.send(message);

            userService.sendpw(email, pw);

            obj.put("success", true);
            obj.put("message", "해당 이메일로 임시 비밀번호가 전송되었습니다.");
        } else {
            obj.put("success", false);
            obj.put("message", "인증번호가 틀렸습니다.");
        }
        return obj;
    }

    @GetMapping("Oauthjoin")
    public void getOauthLogin(Authentication authentication, Model model){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String provider = principalDetails.getUserDto().getProvider();
        model.addAttribute("provider", provider);
    }

    @PostMapping("Oauthjoin")
    public String postOauthLogin(@Valid UserDto dto, BindingResult bindingResult, Model model, HttpServletRequest request){
        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/user/Oauthjoin";
        }

        boolean isJoin = userService.OauthMemberJoin(dto, request, model);

        if (isJoin) {
            return "redirect:/";
        } else
            return "redirect:/user/join?msg=Join Failed";
    }

    @GetMapping(value = "join")
    public void getjoin() {
    }

    @PostMapping("join")
    public String postjoin(@Valid UserDto dto, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response) {

        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/user/join";
        }

        boolean isJoin = userService.memberjoin(dto, model, request, response);

        if (isJoin) {
            return "redirect:/user/login?msg=MemberJoin Success!";
        } else
            return "redirect:/user/join?msg=Join Failed";
    }

    @GetMapping("ConfirmId")
    public @ResponseBody JSONObject ConfirmId(String id, HttpServletResponse response) {
        boolean IdValid = userService.idValid(id);
        JSONObject obj = new JSONObject();
        if (!Objects.equals(id, "") && IdValid) {
            obj.put("success", true);
            obj.put("message", "사용가능한 아이디입니다.");

            TokenInfo tokenInfo = jwtTokenProvider.generateToken("IdAuth", id, true);
            Cookie cookie = new Cookie("IdAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/user");
            response.addCookie(cookie);

            return obj;

        } else if (Objects.equals(id, "")) {
            obj.put("success", false);
            obj.put("message", "아이디를 입력하세요");

            return obj;
        }
        obj.put("success", false);
        obj.put("message", "동일한 아이디가 존재합니다.");

        return obj;
    }

    @GetMapping("ConfirmEmail/{email}")
    public @ResponseBody JSONObject confirmEmail(@PathVariable String email){
        JSONObject obj = new JSONObject();
        if (!Objects.equals(email, "")) {
            obj.put("success", true);
            obj.put("message", "사용가능한 이메일입니다.");
        } else {
            obj.put("success", false);
            obj.put("message", "이메일을 입력하세요");
        }
        return obj;
    }

    @GetMapping("sendEmail/{email}")
    public @ResponseBody ResponseEntity<JSONObject> SendEmail(@PathVariable("email") String email) throws NoSuchAlgorithmException {

        //랜덤 임시번호 6자리 생성
        SecureRandom random = SecureRandom.getInstanceStrong();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        Email_code = builder.toString();

        //메일 메시지 만들기
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[HOTEL] 이메일 인증코드 발급");
        message.setText(Email_code);

        javaMailSender.send(message);


        return new ResponseEntity(new JSONObject().put("success", true), HttpStatus.OK);
    }

    @GetMapping("confirmCode")
    public @ResponseBody JSONObject ConfirmCode(String email, String code, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        if (Objects.equals(Email_code, code)) {
            obj.put("success", true);
            obj.put("message", "이메일이 인증되었습니다.");

            TokenInfo tokenInfo = jwtTokenProvider.generateToken("EmailAuth", email, true);
            Cookie cookie = new Cookie("EmailAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/user");
            response.addCookie(cookie);

            return obj;
        }
        obj.put("success", false);
        obj.put("message", "이메일 인증코드가 다릅니다.");

        return obj;
    }

    class PhoneNumberEditor extends PropertyEditorSupport {
        @Override
        public String getAsText() {
            return (String) getValue();
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            String formattedText = text.replaceAll("-", "");
            setValue(formattedText);
        }
    }
}
