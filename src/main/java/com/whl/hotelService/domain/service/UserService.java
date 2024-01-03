package com.whl.hotelService.domain.service;

import com.whl.hotelService.domain.dto.UserDto;
import com.whl.hotelService.domain.entity.User;
import com.whl.hotelService.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean memberjoin(UserDto dto){
        User user = new User();
        user.setUser_id(dto.getUser_id());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return userRepository.existsById(user.getUser_id());
    }
}
