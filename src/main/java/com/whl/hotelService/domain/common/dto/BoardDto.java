package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

//DTO(Data Transfer Object) 파라미터들을 전송하기 위한 Dto
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든필드를 매개변수로 하는 생성자
public class BoardDto {
    private Long id;
    @NotBlank(message = "작성자를 입력하세요")
    private String boardWriter; //작성자
    @NotBlank(message = "비밀번호를 입력하세요")
    private String boardPassword; //비밀번호
    @NotBlank(message = "제목을 입력하세요")
    private String boardTitle; //제목
    @NotBlank(message = "내용을 입력하세요")
    private String boardContents; //내용
    private int boardHits; //조회수
    private LocalDateTime boardCreatedTime; // 작성시간
    private LocalDateTime boardUpdatedTime; // 수정시간

}


