package com.whl.hotelService.domain.common.entity;

import io.jsonwebtoken.io.IOException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class NoticeImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private int size;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    public NoticeImage(MultipartFile file) throws IOException, java.io.IOException {
        super();
        this.name = file.getOriginalFilename();
        this.type = file.getContentType();
        this.size = (int) file.getSize();
        this.data = file.getBytes();
    }
}
