package com.whl.hotelService.service;

import com.whl.hotelService.config.DataSourceConfig;
import com.whl.hotelService.config.auth.PrincipalDetails;
import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.domain.common.entity.Payment;
import com.whl.hotelService.domain.common.entity.Reservation;
import com.whl.hotelService.domain.common.repository.PaymentRepository;
import com.whl.hotelService.domain.common.repository.ReservationRepository;
import com.whl.hotelService.domain.user.dto.UserDto;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.DataSource;
import java.beans.Transient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Service
public class MyinfoService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private DataSource dataSource;

    public void ReadMyinfo(String id, Model model){
        User user = userRepository.findById(id).get();
        UserDto dto = User.entityToDto(user);

        model.addAttribute("userid", dto.getUserid());
        model.addAttribute("name", dto.getName());
        model.addAttribute("email", dto.getEmail());
        model.addAttribute("phone", dto.getPhone());
        model.addAttribute("zipcode", dto.getZipcode());
        model.addAttribute("addr1", dto.getAddr1());
        model.addAttribute("addr2", dto.getAddr2());
        model.addAttribute("provider", dto.getProvider());
    }

    public boolean isValid(String password, Authentication authentication){
        User user = userRepository.getReferenceById(authentication.getName());
        UserDto dto = User.entityToDto(user);
        return passwordEncoder.matches(password, dto.getPassword());
    }

    @Transactional
    public boolean UpdateInfo(UserDto dto, Authentication authentication, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        PrincipalDetails principalDetail = (PrincipalDetails)authentication.getPrincipal();

        if(!Objects.equals(dto.getUserid(), principalDetail.getUserDto().getUserid())) {
            if (userRepository.existsById(dto.getUserid())) {
                redirectAttributes.addAttribute("userid_msg", "동일한 아이디가 존재합니다.");
                return false;
            }

            Cookie[] idCookies = request.getCookies();
            String idJwtAccessToken = Arrays.stream(idCookies).filter(co -> co.getName().equals("IdAuth")).findFirst()
                    .map(co -> co.getValue())
                    .orElse(null);

            //id jwt토큰 확인
            if (idJwtAccessToken == null) {
                redirectAttributes.addAttribute("userid_msg", "아이디의 중복여부확인이 필요합니다.");
                return false;

            } else if (!jwtTokenProvider.validateToken(idJwtAccessToken)) {
                redirectAttributes.addAttribute("userid_msg", "아이디의 중복여부확인 유효시간을 초과했습니다");
                return false;

            } else {
                Claims claims = jwtTokenProvider.parseClaims(idJwtAccessToken);
                Boolean isIdAuth = (Boolean) claims.get("IdAuth");
                String id = (String) claims.get("id");
                if (isIdAuth == null || !isIdAuth) {
                    redirectAttributes.addAttribute("userid_msg", "아이디의 중복여부확인이 필요합니다.");
                    return false;
                }
                if (!id.equals(dto.getUserid())) {
                    redirectAttributes.addAttribute("userid_msg", "아이디가 변동되었습니다.");
                    return false;
                }
            }
        }
        if(!Objects.equals(dto.getEmail(), principalDetail.getUserDto().getEmail())){
            Cookie[] emailCookies = request.getCookies();
            String emailJwtAccessToken = Arrays.stream(emailCookies).filter(co -> co.getName().equals("EmailAuth")).findFirst()
                    .map(co -> co.getValue())
                    .orElse(null);

            //email jwt토큰 확인
            if(emailJwtAccessToken == null){
                redirectAttributes.addAttribute("email_msg", "이메일 인증이 필요합니다.");
                return false;
            } else if (!jwtTokenProvider.validateToken(emailJwtAccessToken)) {
                redirectAttributes.addAttribute("email_msg", "이메일 인증 유효시간을 초과하였거나 인증을 하지 않았습니다.");
                return false;
            } else {
                Claims claims = jwtTokenProvider.parseClaims(emailJwtAccessToken);
                Boolean isEmailAuth = (Boolean) claims.get("EmailAuth");
                String id = (String) claims.get("id");
                if (isEmailAuth == null || !isEmailAuth) {
                    redirectAttributes.addAttribute("email_msg", "이메일 인증이 필요합니다.");
                    return false;
                }
                if (!id.equals(dto.getEmail())) {
                    redirectAttributes.addAttribute("email_msg", "이메일이 변동되었습니다..");
                    return false;
                }
            }
        }

        User user = userRepository.findById(authentication.getName()).get();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setZipcode(dto.getZipcode());
        user.setAddr1(dto.getAddr1());
        user.setAddr2(dto.getAddr2());

        userRepository.save(user);

        return userRepository.existsById(user.getUserid());
    }

    @Transactional
    public void IdUpdate(UserDto dto) throws SQLException {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if(!Objects.equals(dto.getUserid(), principalDetails.getUserDto().getUserid())) {
            String update_sql = "UPDATE user SET userid = ? WHERE userid = ?";
            Connection conn = null;

            conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(update_sql);

            // 파라미터 설정
            preparedStatement.setString(1, dto.getUserid());
            preparedStatement.setString(2, authentication.getName());

            // 쿼리 실행
            int affectedRows = preparedStatement.executeUpdate();

            // 연결 닫기
            conn.close();

        }
    }

    @Transactional
    public boolean DeleteInfo(String password, String word, Authentication authentication, RedirectAttributes redirectAttributes){
        User user = userRepository.findById(authentication.getName()).get();
        if(!passwordEncoder.matches(password, user.getPassword())){
            redirectAttributes.addAttribute("msg", "비밀번호가 다릅니다.");
            return false;
        }else if(!Objects.equals("위의 내용을 인지하였으며 회원탈퇴에 동의합니다.", word)){
            redirectAttributes.addAttribute("msg", "동의 확인 문장이 다릅니다.");
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    public List<Reservation> FindUserReservation(Authentication authentication) {
        List<Reservation> AllReservationList = reservationRepository.findAll();
        List<Reservation> UserReservationList = new ArrayList<>();
        for (Reservation reservation : AllReservationList) {
            if (Objects.equals(reservation.getUser().getUserid(), authentication.getName()))
                UserReservationList.add(reservation);
        }
        return UserReservationList;
    }

    public Payment FindUserPayment(Long id) {
        return paymentRepository.findByReservationId(id);
    }
}
