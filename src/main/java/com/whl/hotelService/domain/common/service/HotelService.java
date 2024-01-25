package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.PaymentDto;
import com.whl.hotelService.domain.common.dto.ReservationDto;
import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.Payment;
import com.whl.hotelService.domain.common.entity.Reservation;
import com.whl.hotelService.domain.common.entity.Room;
import com.whl.hotelService.domain.common.repository.HotelRepository;
import com.whl.hotelService.domain.common.repository.PaymentRepository;
import com.whl.hotelService.domain.common.repository.ReservationRepository;
import com.whl.hotelService.domain.common.repository.RoomRepository;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
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

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    public List<String> getDistinctRegion() {
        return hotelRepository.findDistinctRegion();
    }

    public List<Room> getHotelsRoom(String hotelname, int people) {
        return roomRepository.findByHotelHotelnameAndStandardPeopleGreaterThanEqual(hotelname, people);
    }



    @Transactional
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
                existingReservation.setCreatedAt(new Date());
                existingReservation.setPrice(reservationDto.getPrice());

                try {
                    reservationRepository.save(existingReservation);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    return false;
                }
            } else {
                // Create a new reservation
                Reservation reservation = new Reservation();
                reservation.setRoom(room);
                reservation.setUser(user);
                reservation.setStatus(reservationDto.getStatus());
                reservation.setCheckin(reservationDto.getCheckin());
                reservation.setCheckout(reservationDto.getCheckout());
                reservation.setPeople(reservationDto.getPeople());
                reservation.setCreatedAt(new Date());
                reservation.setPrice(reservationDto.getPrice());


                try {
                    reservationRepository.save(reservation);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    return false;
                }
            }
        }


        return true;
    }

    @Transactional
    public Reservation getReservationList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        return reservationRepository.findByUserUseridAndStatus(userid, "예약 중");
    }

    @Transactional
    public boolean addPayment(PaymentDto paymentDto) {
        Reservation reservation = reservationRepository.findById(paymentDto.getReservationId()).get();
        reservation.setStatus("예약 완료");
        System.out.println(reservation);
        reservationRepository.save(reservation);

        Payment payment = new Payment();
        payment.setAddress(paymentDto.getAddress());
        payment.setImpUid(paymentDto.getImpUid());
        payment.setName(paymentDto.getName());
        payment.setPaidAmount(paymentDto.getPaidAmount());
        payment.setPayMethod(paymentDto.getPayMethod());
        payment.setMerchantUid(paymentDto.getMerchantUid());
        payment.setStatus(paymentDto.getStatus());
        payment.setPayDate(LocalDateTime.now());
        payment.setReservation(reservation);

        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user =  userRepository.findById(username).get();
        payment.setUserid(user);
        paymentRepository.save(payment);



        return true;
    }
}
