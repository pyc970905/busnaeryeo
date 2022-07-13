package io.bit.busnaeryeo.domain.dto;

import io.bit.busnaeryeo.domain.etity.Notice;
import io.bit.busnaeryeo.domain.etity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeDTO {

    private Long id;
    private String title;
    private String content;
    private User user;


    public Notice ToEntity() {

         Notice notice = Notice.builder()
                 .id(id)
                 .title(title)
                 .content(content)
                 .user(user)
                 .build();

        return notice;
    }
}
