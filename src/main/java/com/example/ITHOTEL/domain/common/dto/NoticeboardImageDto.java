package com.example.ITHOTEL.domain.common.dto;

import com.example.ITHOTEL.domain.common.entity.NoticeBoard;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeboardImageDto {
    private Long id;
    private String name;
    private String type;
    private int size;
    private byte[] data;
    private NoticeBoard noticeBoard;
}
