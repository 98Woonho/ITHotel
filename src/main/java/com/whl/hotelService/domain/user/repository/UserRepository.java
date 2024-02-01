package com.whl.hotelService.domain.user.repository;

import com.whl.hotelService.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(@Param("email") String email);
    User findByNameAndEmail(@Param("name") String name, @Param("email") String email);

}
