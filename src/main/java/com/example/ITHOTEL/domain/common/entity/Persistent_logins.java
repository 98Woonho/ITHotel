package com.example.ITHOTEL.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Persistent_logins {
    @Id
    @Column(name="series",nullable = false,length=64)
    String series;       // 로그인 유지를 위한 고정값
    String username;     // 로그인 시 아이디
    String token;        // 로그인 시 발급받은 token값
    Timestamp last_used; // 마지막 로그인 시간
}
