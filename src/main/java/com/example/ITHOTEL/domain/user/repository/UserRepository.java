package com.example.ITHOTEL.domain.user.repository;

import com.example.ITHOTEL.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(@Param("email") String email);
    User findByNameAndEmail(@Param("name") String name, @Param("email") String email);

}
