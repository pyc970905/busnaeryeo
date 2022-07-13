package io.bit.busnaeryeo.controller;

import io.bit.busnaeryeo.domain.dto.NoticeDTO;
import io.bit.busnaeryeo.domain.etity.Notice;
import io.bit.busnaeryeo.service.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;

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
@RequestMapping("/api//notice")
public class NoticeController {

    private final NoticeServiceImpl noticeService;

    //공지 보기
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNotices(@PageableDefault Pageable pageable, PagedResourcesAssembler<Notice> assembler) {
        Page<Notice> notices = noticeService.findAll(pageable);
        PagedModel<EntityModel<Notice>> model = assembler.toModel(notices);
        return  new ResponseEntity(model, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> postNotice(@RequestBody NoticeDTO noticeDTO) {

        Notice persistNotice = (noticeService.save(noticeDTO));
        NoticeDTO saveNotice = persistNotice.ToDTO();
        return new ResponseEntity<>(saveNotice, HttpStatus.CREATED);
    }

    //공지 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> putNotice(@PathVariable("id") Long id, @RequestBody NoticeDTO noticeDTO) {
        Notice persistNotice = noticeService.findNoticeById(id);
        NoticeDTO modifyNotice = persistNotice.ToDTO();
        NoticeDTO saveNotice = noticeService.save(modifyNotice).ToDTO();
        return new ResponseEntity<>(saveNotice, HttpStatus.OK);
    }

    //공지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("id") Long id) {
        noticeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}