package com.whl.hotelService.domain.repository;

import com.whl.hotelService.domain.entity.User;
import net.minidev.json.JSONUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
