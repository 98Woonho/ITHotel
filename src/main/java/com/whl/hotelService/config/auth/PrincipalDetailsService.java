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
        System.out.println("UserDetails user_id : " + username);
        Optional<User> userOptional = userRepository.findById(username);
        if(userOptional.isEmpty())
            return null;

        //Entity -> Dto
        UserDto dto = new UserDto();
        dto.setUser_id(userOptional.get().getUser_id());
        dto.setPassword(userOptional.get().getPassword());
        dto.setRole(userOptional.get().getRole());

        return new PrincipalDetails(dto);
    }
}
