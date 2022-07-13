package io.bit.busnaeryeo.domain.etity;

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
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne
    private User user;






    public NoticeDTO ToDTO() {

        NoticeDTO noticeDTO = NoticeDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .user(user)
                .build();

        return noticeDTO;
    }
}