package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.ReservationDto;
import com.whl.hotelService.domain.common.entity.*;
import com.whl.hotelService.domain.common.repository.*;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservedRoomCountRepository reservedRoomCountRepository;

    @Autowired
    private RoomFileInfoRepository roomFileInfoRepository;

    @Transactional(rollbackFor = Exception.class)
    public Payment getPayment(Long id) {
        return paymentRepository.findByReservationId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Room> getHotelsRoom(String hotelname, int people) {
        return roomRepository.findByHotelHotelNameAndStandardPeopleGreaterThanEqual(hotelname, people);
    }

    @Scheduled(fixedRate = 1000) // 해당 메서드를 1000ms 주기로 실행
    @Transactional(rollbackFor = Exception.class)
    // reservation table에서 status="예약 중" 인 데이터가 생성 시점으로부터 10분이 넘을 시 자동으로 삭제해 줌.
    public void deleteExpiredReservations() {
        List<LocalDateTime> createdAtList = reservationRepository.findCreatedAtByStatus("예약 중");
        LocalDateTime currentDate = LocalDateTime.now();

        for(LocalDateTime createdAt : createdAtList) {
            LocalDateTime expiredAt = createdAt.plusMinutes(10);

            if (currentDate.isAfter(expiredAt)) {
                reservationRepository.deleteByCreatedAt(createdAt);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int getReservedRoomCount(String date, Long roomId) {
        ReservedRoomCount reservedRoomCount = reservedRoomCountRepository.findByDateAndRoomId(date, roomId);

        return (reservedRoomCount != null) ? reservedRoomCount.getReservedCount() : 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addReservedRoomCount(Long id, int reservedRoomCount) {
        Room room = roomRepository.findById(id).get();

        room.setReservedRoomCount(reservedRoomCount);

        roomRepository.save(room);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<String> getDistinctRegion() {
        return hotelRepository.findDistinctRegion();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean insertReservation(ReservationDto reservationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();

        User user = userRepository.findById(userid).get();

        Room room = roomRepository.findById(reservationDto.getRoomId()).get();

        String status = reservationDto.getStatus(); // "예약 중"

        if (user != null && room != null) {
            Reservation existingReservation = reservationRepository.findByUserUseridAndStatus(userid, status);

            // 이미 해당 계정으로 같은 객실의 status="예약 중" 인 예약 정보가 있으면
            if (existingReservation != null) {
                existingReservation.setCheckin(reservationDto.getCheckin());
                existingReservation.setCheckout(reservationDto.getCheckout());
                existingReservation.setRoom(room);
                existingReservation.setPeople(reservationDto.getPeople());
                existingReservation.setCreatedAt(LocalDateTime.now());
                existingReservation.setPrice(reservationDto.getPrice());

                reservationRepository.save(existingReservation);

            } else {
                Reservation reservation = new Reservation();
                reservation.setRoom(room);
                reservation.setUser(user);
                reservation.setStatus(reservationDto.getStatus());
                reservation.setCheckin(reservationDto.getCheckin());
                reservation.setCheckout(reservationDto.getCheckout());
                reservation.setPeople(reservationDto.getPeople());
                reservation.setCreatedAt(LocalDateTime.now());
                reservation.setPrice(reservationDto.getPrice());

                reservationRepository.save(reservation);
            }
        }
        return true;
    }

    public List<RoomFileInfo> getAllMainFiles(String hotelname) {
        return roomFileInfoRepository.findAllMainFiles(hotelname);
    }

    // 예약을 취소할 때, 예약된 객실 개수 차감
    @Transactional(rollbackFor = Exception.class)
    public void deleteReservationRoomCount(Long id) {
        Reservation reservation = reservationRepository.findById(id).get();

        Room room = reservation.getRoom();

        // 날짜 문자열을 LocalDate로 변환
        LocalDate date1 = LocalDate.parse(reservation.getCheckin());
        LocalDate date2 = LocalDate.parse(reservation.getCheckout());

        // 두 날짜 사이의 일수 계산
        long daysDifference = ChronoUnit.DAYS.between(date1, date2);

        // 두 날짜를 포함한 사이의 날짜 List 생성
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 0; i <= daysDifference - 1; i++) {
            dateList.add(date1.plusDays(i));
        }

        for (LocalDate date : dateList) {
            ReservedRoomCount existingReservedRoomCount = reservedRoomCountRepository.findByDateAndRoomId(String.valueOf(date), room.getId());

            existingReservedRoomCount.setReservedCount(existingReservedRoomCount.getReservedCount() - 1);

            reservedRoomCountRepository.save(existingReservedRoomCount);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean DeleteReservedRoomCount(int reservationId){
        Long reservedRoomId = reservationRepository.findById((long)reservationId).get().getRoom().getId();
        String checkInDate = reservationRepository.findById((long)reservationId).get().getCheckin();
        ReservedRoomCount reservedRoomCount = reservedRoomCountRepository.findByDateAndRoomId(checkInDate, reservedRoomId);
        reservedRoomCountRepository.delete(reservedRoomCount);
        return reservedRoomCountRepository.findById(reservedRoomId).isEmpty();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean DeleteReservation(int id) {
        Reservation reservation = reservationRepository.findById((long) id).get();
        reservationRepository.delete(reservation);
        return reservationRepository.findById((long) id).isEmpty();
    }
}
