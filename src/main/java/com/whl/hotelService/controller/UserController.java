package com.whl.hotelService.controller;

import com.whl.hotelService.config.auth.jwt.JwtProperties;
import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.config.auth.jwt.TokenInfo;
import com.whl.hotelService.Userdomain.dto.UserDto;
import com.whl.hotelService.Userdomain.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

import static org.apache.ibatis.ognl.Ognl.setValue;

@Slf4j
@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    String Email_code;

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        log.info("databinder obj : " + dataBinder);
        dataBinder.registerCustomEditor(String.class, "phone", new PhoneNumberEditor());
    }

    @GetMapping(value = "login")
    public void getLogin() {
        log.info("getLogin()");
    }

    @GetMapping(value = "join")
    public void getjoin() {
        log.info("getjoin()");
    }

    @PostMapping("join")
    public String postjoin(@Valid UserDto dto, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.info("postjoin() dto : " + dto);

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

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("ConfirmId")
    public @ResponseBody JSONObject ConfirmId(String user_id, HttpServletResponse response) {
        log.info("getConfirmId id : " + user_id);
        boolean IdValid = userService.idValid(user_id);
        JSONObject obj = new JSONObject();
        if (!Objects.equals(user_id, "") && IdValid) {
            obj.put("success", true);
            obj.put("message", "사용가능한 아이디입니다.");

            TokenInfo tokenInfo = jwtTokenProvider.generateToken("IdAuth", user_id, true);
            Cookie cookie = new Cookie("IdAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
            response.addCookie(cookie);

            return obj;

        } else if (Objects.equals(user_id, "")) {
            obj.put("success", false);
            obj.put("message", "아이디를 입력하세요");

            return obj;
        }
        obj.put("success", false);
        obj.put("message", "동일한 아이디가 존재합니다.");

        return obj;
    }

    @GetMapping("sendEmail/{email}")
    public @ResponseBody ResponseEntity<JSONObject> SendEmail(@PathVariable("email") String email) throws NoSuchAlgorithmException {
        log.info("getSendEmail : " + email);

        //랜덤 임시번호 6자리 생성
        SecureRandom random = SecureRandom.getInstanceStrong();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        Email_code = builder.toString();
        System.out.println(Email_code);

        //메일 메시지 만들기
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[HOTEL] 이메일 인증코드 발급");
        message.setText(Email_code);

        javaMailSender.send(message);


        return new ResponseEntity(new JSONObject().put("success", true), HttpStatus.OK);
    }

    @GetMapping("confirmEmail")
    public @ResponseBody JSONObject ConfirmEmail(String email, String code, HttpServletResponse response) {
        log.info("getConfirmEmail email : " + email + ", code : " + code);
        JSONObject obj = new JSONObject();
        if (Objects.equals(Email_code, code)) {
            obj.put("success", true);
            obj.put("message", "이메일이 인증되었습니다.");

            TokenInfo tokenInfo = jwtTokenProvider.generateToken("EmailAuth", email, true);
            Cookie cookie = new Cookie("EmailAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
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
