package com.whl.hotelService.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 상속받아서 사용해야 한다는 것을 지정
//@EntityListeners(AuditingEntityListener.class)를 활용하면 엔터티에 대한 생성 및 수정 시간을 자동으로 기록할 수 있습니다.
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {
    @CreationTimestamp //생성됬을 때 시간 작성Date.Now()와 같음
    @Column(updatable = false) // 수정됬을 때는 관여 안함
    private LocalDateTime createdTime;
    @UpdateTimestamp // 수정했을 때 시간 작성
    @Column(insertable = false) // 작성했을 때는 관여 안함
    private LocalDateTime updatedTime;
}
