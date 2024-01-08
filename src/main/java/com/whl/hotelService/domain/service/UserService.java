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

    public boolean idValid(String id){
        if(userRepository.existsById(id))
            return false;
        return true;
    }

    public boolean memberjoin(UserDto dto){
        User user = new User();
        user.setUser_id(dto.getUser_id());
        user.setPassword(dto.getPassword());
        user.setRepassword(dto.getRepassword());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setZipcode(dto.getZipcode());
        user.setAddr1(dto.getAddr1());
        user.setAddr2(dto.getAddr2());
        user.setRole(dto.getRole());
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return userRepository.existsById(user.getUser_id());
    }
}
