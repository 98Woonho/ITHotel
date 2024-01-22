package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.ReservationDto;
import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.Reservation;
import com.whl.hotelService.domain.common.entity.Room;
import com.whl.hotelService.domain.common.repository.HotelRepository;
import com.whl.hotelService.domain.common.repository.ReservationRepository;
import com.whl.hotelService.domain.common.repository.RoomRepository;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Hotel> getHotel() {
        return hotelRepository.findAll();
    }

    public List<String> getRegion() {
        return hotelRepository.findDistinctRegion();
    }

    public List<Room> getRoom(String hotelname) {
        return roomRepository.findByHotelHotelname(hotelname);
    }

    @Transactional
    public boolean insertReservation(ReservationDto reservationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();

        User user = userRepository.findById(userid).get();

        Room room = roomRepository.findById(reservationDto.getRoomId()).get();

        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setCheckin(reservationDto.getCheckin());
        reservation.setCheckout(reservationDto.getCheckout());

        if (user != null && room != null) {
            // Check if a reservation already exists for the user and room
            Reservation existingReservation = reservationRepository.findByUserUseridAndRoomId(userid, room.getId());

            if (existingReservation != null) {
                // Update existing reservation
                existingReservation.setCheckin(reservationDto.getCheckin());
                existingReservation.setCheckout(reservationDto.getCheckout());

                try {
                    reservationRepository.save(existingReservation);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    return false;
                }
            } else {
                // Create a new reservation
                Reservation newReservation = new Reservation();
                newReservation.setRoom(room);
                newReservation.setUser(user);
                newReservation.setCheckin(reservationDto.getCheckin());
                newReservation.setCheckout(reservationDto.getCheckout());

                try {
                    reservationRepository.save(newReservation);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    return false;
                }
            }
        }

        return false;
    }

    public Reservation getReservationList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        return reservationRepository.findByUserUserid(userid);
    }
}
