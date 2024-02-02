package com.whl.hotelService.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardWriteRequestDto {

    private String title;
    private String content;
    private String hotelname;
    private String relation;
}
