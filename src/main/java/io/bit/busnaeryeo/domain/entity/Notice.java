package io.bit.busnaeryeo.domain.entity;

import io.bit.busnaeryeo.domain.dto.NoticeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends Time{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;
    @Column
    private  String writer;
    @Column
    private String content;




    public NoticeDTO ToDTO() {

        NoticeDTO noticeDTO = NoticeDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .build();

        return noticeDTO;
    }
}