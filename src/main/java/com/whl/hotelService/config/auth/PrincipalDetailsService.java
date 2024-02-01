package com.whl.hotelService.config.auth;

import com.whl.hotelService.domain.user.dto.UserDto;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findById(username);
        if(userOptional.isEmpty())
            return null;

        //Entity -> Dto
        UserDto dto = new UserDto();
        dto.setUserid(userOptional.get().getUserid());
        dto.setPassword(userOptional.get().getPassword());
        dto.setName(userOptional.get().getName());
        dto.setEmail(userOptional.get().getEmail());
        dto.setPhone(userOptional.get().getPhone());
        dto.setZipcode(userOptional.get().getZipcode());
        dto.setAddr1(userOptional.get().getAddr1());
        dto.setAddr2(userOptional.get().getAddr2());
        dto.setRole(userOptional.get().getRole());

        return new PrincipalDetails(dto);
    }
}
