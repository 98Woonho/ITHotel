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
    public List<Room> getHotelsRoom(String hotelname, int people) {
        return roomRepository.findByHotelHotelNameAndStandardPeopleGreaterThanEqual(hotelname, people);
    }

//    @Scheduled(fixedRate = 1000000)
    @Transactional(rollbackFor = Exception.class)
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

        String status = reservationDto.getStatus();

        if (user != null && room != null) {
            // Check if a reservation already exists for the user and room
            Reservation existingReservation = reservationRepository.findByUserUseridAndStatus(userid, status);

            if (existingReservation != null) {
                // Update existing reservation
                existingReservation.setCheckin(reservationDto.getCheckin());
                existingReservation.setCheckout(reservationDto.getCheckout());
                existingReservation.setRoom(room);
                existingReservation.setPeople(reservationDto.getPeople());
                existingReservation.setCreatedAt(LocalDateTime.now());
                existingReservation.setPrice(reservationDto.getPrice());

                reservationRepository.save(existingReservation);

            } else {
                // Create a new reservation
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

    @Transactional(rollbackFor = Exception.class)
    public Reservation getReservationList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        return reservationRepository.findByUserUseridAndStatus(userid, "예약 중");
    }

    public String addReservedRoomCount(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).get();

        Room room = reservation.getRoom();

        // Parse the strings to LocalDate objects
        LocalDate date1 = LocalDate.parse(reservation.getCheckin());
        LocalDate date2 = LocalDate.parse(reservation.getCheckout());

        // Calculate the number of days between the two dates
        long daysDifference = ChronoUnit.DAYS.between(date1, date2);

        // Create a list containing LocalDate objects for each day between the two dates
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 0; i <= daysDifference - 1; i++) {
            dateList.add(date1.plusDays(i));
        }

        for (LocalDate date : dateList) {
            ReservedRoomCount existingReservedRoomCount = reservedRoomCountRepository.findByDateAndRoomId(String.valueOf(date), room.getId());

            if (existingReservedRoomCount != null) {
                if (existingReservedRoomCount.getReservedCount() == room.getCount()) {
                    reservationRepository.deleteById(reservationId);
                    return "FAILURE_NOVACANCY";
                } else {
                    existingReservedRoomCount.setReservedCount(existingReservedRoomCount.getReservedCount() + 1);
                }

                try {
                    reservedRoomCountRepository.save(existingReservedRoomCount);
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    return "FAILURE";
                }
            } else {
                ReservedRoomCount reservedRoomCount = new ReservedRoomCount();
                reservedRoomCount.setDate(String.valueOf(date));
                reservedRoomCount.setRoom(room);

                try {
                    reservedRoomCountRepository.save(reservedRoomCount);
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    return "FAILURE";
                }
            }
        }
        return "SUCCESS";
    }

    public boolean DeleteReservedRoomCount(int reservationId){
        Long reservedRoomId = reservationRepository.findById((long)reservationId).get().getRoom().getId();
        String checkInDate = reservationRepository.findById((long)reservationId).get().getCheckin();
        ReservedRoomCount reservedRoomCount = reservedRoomCountRepository.findByDateAndRoomId(checkInDate, reservedRoomId);
        reservedRoomCountRepository.delete(reservedRoomCount);
        return reservedRoomCountRepository.findById(reservedRoomId).isEmpty();
    }

    @Transactional
    public boolean DeleteReservation(int id) {
        Reservation reservation = reservationRepository.findById((long) id).get();
        reservationRepository.delete(reservation);
        return reservationRepository.findById((long) id).isEmpty();
    }
}
