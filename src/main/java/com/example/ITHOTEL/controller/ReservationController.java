package com.example.ITHOTEL.controller;

import com.example.ITHOTEL.domain.common.dto.ReservationDto;
import com.example.ITHOTEL.domain.common.entity.Hotel;
import com.example.ITHOTEL.domain.common.entity.Payment;
import com.example.ITHOTEL.domain.common.entity.Room;
import com.example.ITHOTEL.domain.common.entity.RoomFileInfo;
import com.example.ITHOTEL.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "reservation")
public class  ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping(value = "select")
    public void getReservationStep1(@RequestParam(value = "hotelName") String hotelName,
                                    @RequestParam(value = "checkin")
                                    String checkin,
                                    @RequestParam(value = "checkout")
                                    String checkout,
                                    @RequestParam(value = "adultCount") int adultCount,
                                    @RequestParam(value = "childCount") int childCount,
                                    Model model) {
        int people = adultCount + childCount;
        List<Room> roomList = reservationService.getHotelsRoom(hotelName, people);


        for(Room room : roomList) {
            // checkin 날짜와 checkout 날짜를 LocalDate로 변환
            LocalDate startDate = LocalDate.parse(checkin);
            LocalDate endDate = LocalDate.parse(checkout);

            // 날짜 목록을 저장할 리스트
            List<String> dateList = new ArrayList<>();

            // checkin 날짜와 checkout 날짜 까지 반복하여 날짜 목록에 추가
            while (!startDate.isEqual(endDate)) {
                dateList.add(startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                startDate = startDate.plusDays(1);
            }

            List<Integer> reservedRoomCountList = new ArrayList<>();
            // 날짜 List를 반복
            for(String date : dateList) {
                // 해당 날짜에 예약된 객실 개수를 List로 저장
                int reservedRoomCount = reservationService.getReservedRoomCount(date, room.getId());
                reservedRoomCountList.add(reservedRoomCount);
            }
            // 객실 개수 List에서 가장 큰 예약된 방 개수를 int reservedMaxRoomCount에 초기화
            int reservedMaxRoomCount = Collections.max(reservedRoomCountList);
            reservationService.addReservedRoomCount(room.getId(), reservedMaxRoomCount);
        }

        List<Hotel> hotelList = reservationService.getAllHotel();
        model.addAttribute("hotelList", hotelList);
        List<String> region = reservationService.getDistinctRegion();
        model.addAttribute("region", region);

        model.addAttribute("hotelName", hotelName);
        model.addAttribute("checkin", checkin);
        model.addAttribute("checkout", checkout);
        model.addAttribute("adultCount", adultCount);
        model.addAttribute("childCount", childCount);

        // 주중, 주말 가격 계산을 위한 요일 카운트 리스트 생성 코드
        // [금, 토, 주중]
        LocalDate startDate = LocalDate.parse(checkin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(checkout, DateTimeFormatter.ofPattern("yyyy-MM-dd")).minusDays(1);

        List<DayOfWeek> daysOfWeekList = new ArrayList<>();

        // 날짜를 반복하고 목록에 요일을 추가
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            daysOfWeekList.add(currentDate.getDayOfWeek());
            currentDate = currentDate.plusDays(1);
        }

        DayOfWeek[] daysOfWeekBetweenDates = daysOfWeekList.toArray(new DayOfWeek[0]);

        long fridayCount = Arrays.stream(daysOfWeekBetweenDates)
                .filter(dayOfWeek -> dayOfWeek == DayOfWeek.FRIDAY)
                .count();
        long saturdayCount = Arrays.stream(daysOfWeekBetweenDates)
                .filter(dayOfWeek -> dayOfWeek == DayOfWeek.SATURDAY)
                .count();
        long weekdayCount = Arrays.stream(daysOfWeekBetweenDates)
                .filter(dayOfWeek -> dayOfWeek != DayOfWeek.FRIDAY && dayOfWeek != DayOfWeek.SATURDAY)
                .count();

        List<Long> dayCountList = new ArrayList<>();

        dayCountList.add(fridayCount);
        dayCountList.add(saturdayCount);
        dayCountList.add(weekdayCount);

        model.addAttribute("dayLength", daysOfWeekBetweenDates.length); // n박
        model.addAttribute("dayCountList", dayCountList);

        List<RoomFileInfo> mainFileList = reservationService.getAllMainFiles(hotelName);

        model.addAttribute("mainFileList", mainFileList);
    }

    @PostMapping(value = "select")
    @ResponseBody
    public ResponseEntity<String> postReservationStep1(ReservationDto reservationDto) {
        boolean isInserted = reservationService.insertReservation(reservationDto);

        if (isInserted) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAILURE", HttpStatus.BAD_GATEWAY);
        }
    }

    @DeleteMapping(value="delete/{reservationId}")
    @ResponseBody
    public ResponseEntity<String> deletePayment(@PathVariable("reservationId") Long reservationId) {
        boolean isDeleted = reservationService.deleteReservation(reservationId);

        if(isDeleted) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAILURE", HttpStatus.CONFLICT);
        }
    }

    // 결제 취소용 AccessToken 발급요청
    public String getAccessToken() {
        // URL
        // 아임포트 API의 토큰을 얻기 위한 엔드포인트 URL을 지정
        String url = "https://api.iamport.kr/users/getToken";

        // Request Header
        // HTTP 요청 헤더를 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // Request Body
        // HTTP 요청 바디를 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("imp_key", "7582034642764268");
        params.add("imp_secret", "JxMwheK2PKBrxFxOifDLwwZvdyzjwDERKj4TzStgSZ06Wmg3oQp7h3WjK3nOfdjXsSXF0ZNgCbBWyPrV");

        // Header + Body
        // 요청 헤더와 바디를 합쳐서 HttpEntity 객체를 생성
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(params, headers);

        // 요청
        // RestTemplate을 사용하여 HTTP POST 요청을 보냄. 응답은 ImportAccessTokenResponse에 Mapping됨.
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ImportAccessTokenResponse> resp =  restTemplate.exchange(url, HttpMethod.POST,entity,ImportAccessTokenResponse.class);

        // accessToken return
        return resp.getBody().getResponse().getAccess_token();
    }

    @GetMapping("cancel")
    @ResponseBody
    public void getCancel(@RequestParam(value="reservationId") Long reservationId) {
        // accessToken 받기
        String accessToken = getAccessToken();

        Payment payment = reservationService.getPayment(reservationId);
        String imp_uid = payment.getImpUid();

        // URL
        // 아임포트 결제 취소를 위한 엔드포인트 URL을 지정
        String url = "https://api.iamport.kr/payments/cancel";

        // Request Header
        // HTTP 요청 헤더를 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");

        // Request Body
        // HTTP 요청 바디를 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("imp_uid", imp_uid);

        // Header + Body
        // 요청 헤더와 바디를 합쳐서 HttpEntity 객체를 생성
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(params, headers);

        // 요청
        // RestTemplate을 사용하여 HTTP POST 요청을 보냄. 응답은 String에 Mapping됨.
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // DB 삭제
        reservationService.deleteReservationRoomCount(reservationId);
        reservationService.deleteReservation(reservationId);
    }
}