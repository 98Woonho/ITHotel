package com.whl.hotelService.domain.userDomain.repository;

import com.whl.hotelService.domain.userDomain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
