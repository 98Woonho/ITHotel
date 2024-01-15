package com.whl.hotelService.Userdomain.repository;

import com.whl.hotelService.Userdomain.entity.User;
import net.minidev.json.JSONUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
