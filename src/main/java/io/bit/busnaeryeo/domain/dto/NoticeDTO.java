package io.bit.busnaeryeo.domain.dto;

import io.bit.busnaeryeo.domain.entity.Notice;
import io.bit.busnaeryeo.domain.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class NoticeDTO {

    private Long id;
    private String title;
    private String content;

    private String writer;





    public Notice ToEntity() {

         Notice notice = Notice.builder()
                 .id(id)
                 .title(title)
                 .content(content)
                 .writer(writer)

                 .build();

        return notice;
    }
}
