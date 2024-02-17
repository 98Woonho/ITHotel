package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.NoticeBoard;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeImageDto {
    private Long id;
    private String name;
    private String type;
    private int size;
    private byte[] data;
    private NoticeBoard noticeBoard;
}
