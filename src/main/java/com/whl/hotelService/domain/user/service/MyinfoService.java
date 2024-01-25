package com.whl.hotelService.domain.user.service;

import com.whl.hotelService.domain.user.dto.UserDto;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class MyinfoService {

    @Autowired
    UserRepository userRepository;

    public void ReadMyinfo(String id, Model model){
        User user = userRepository.getReferenceById(id);
        UserDto dto = User.entityToDto(user);

        model.addAttribute("Userid", dto.getUserid());
        model.addAttribute("password", dto.getPassword());
        model.addAttribute("name", dto.getName());
        model.addAttribute("email", dto.getEmail());
        model.addAttribute("phone", dto.getPhone());
        model.addAttribute("zipcode", dto.getZipcode());
        model.addAttribute("addr1", dto.getAddr1());
        model.addAttribute("addr2", dto.getAddr2());
    }
}
