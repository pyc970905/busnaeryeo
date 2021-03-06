package io.bit.busnaeryeo.domain.dto;

import io.bit.busnaeryeo.domain.entity.Notice;
import io.bit.busnaeryeo.domain.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class NoticeDTO {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private User user;

    public Notice toEntity() {

         Notice notice = Notice.builder()
                 .id(id)
                 .title(title)
                 .content(content)
                 .writer(writer)
                 .user(user)
                 .build();

        return notice;
    }
}
