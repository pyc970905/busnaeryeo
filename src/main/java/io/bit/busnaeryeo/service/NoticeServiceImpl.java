package io.bit.busnaeryeo.service;

import io.bit.busnaeryeo.domain.dto.NoticeDTO;
import io.bit.busnaeryeo.domain.entity.Notice;
import io.bit.busnaeryeo.repository.NoticeRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;

    //@Requestparam 으로 pageble이라는 숫자가 들어가고 return을 받아야 할 것
    public Page<Notice>findAll(Pageable pageable) {

        return noticeRepository.findAll(pageable);
    }

    public Notice findNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(new Notice());
        return notice;
    }

    public Notice save(NoticeDTO noticeDTO) {
        Notice saveNotice = noticeRepository.save(noticeDTO.ToEntity());
        return saveNotice;
    }

    public void deleteById(Long id) {
        noticeRepository.deleteById(id);
    }


}
