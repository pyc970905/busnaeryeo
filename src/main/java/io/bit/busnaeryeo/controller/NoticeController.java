package io.bit.busnaeryeo.controller;

import io.bit.busnaeryeo.domain.dto.NoticeDTO;
import io.bit.busnaeryeo.domain.etity.Notice;
import io.bit.busnaeryeo.service.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@Log4j2
public class NoticeController {

    private final NoticeServiceImpl noticeService;

    //공지 보기
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNotices(@PageableDefault Pageable pageable, PagedResourcesAssembler<Notice> assembler) {
        Page<Notice> notices = noticeService.findAll(pageable);
        PagedModel<EntityModel<Notice>> model = assembler.toModel(notices);
        return  new ResponseEntity(model, HttpStatus.CREATED);
    }
    //공지 등록
    @PostMapping("/admin")
    public ResponseEntity<?> postNotice(@RequestBody NoticeDTO noticeDTO) {

        Notice persistNotice = (noticeService.save(noticeDTO));
        NoticeDTO saveNotice = persistNotice.ToDTO();
        return new ResponseEntity<>(saveNotice, HttpStatus.CREATED);
    }

    //공지 수정
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> putNotice(@PathVariable("id") Long id, @RequestBody NoticeDTO noticeDTO) {
        Notice persistNotice = noticeService.findNoticeById(id);
        log.info(persistNotice);
        NoticeDTO modifyNotice = persistNotice.ToDTO();
        log.info(modifyNotice);
        modifyNotice.setContent(noticeDTO.getContent());
        modifyNotice.setTitle(noticeDTO.getTitle());
        NoticeDTO saveNotice = noticeService.save(modifyNotice).ToDTO();
        log.info(saveNotice);
        return new ResponseEntity<>(saveNotice, HttpStatus.OK);
    }

    //공지 삭제
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("id") Long id) {
        noticeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
