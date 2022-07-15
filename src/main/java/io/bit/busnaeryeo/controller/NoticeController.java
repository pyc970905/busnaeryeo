package io.bit.busnaeryeo.controller;

import io.bit.busnaeryeo.domain.dto.NoticeDTO;
import io.bit.busnaeryeo.domain.entity.Notice;
import io.bit.busnaeryeo.repository.NoticeRepository;
import io.bit.busnaeryeo.service.NoticeServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@Log4j2
public class NoticeController {
    private String secretKey = "lalala";
    private final NoticeServiceImpl noticeService;
    private final NoticeRepository noticeRepository;

    //공지 보기
    @GetMapping(value ="/admin" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNotices(@PageableDefault Pageable pageable, PagedResourcesAssembler<Notice> assembler) {
        Page<Notice> notices = noticeService.findAll(pageable);
        PagedModel<EntityModel<Notice>> model = assembler.toModel(notices);
        return  new ResponseEntity(model, HttpStatus.OK);
    }



    //공지 등록
    @PostMapping(value = "/admin")
    public ResponseEntity<?> postNotice(HttpServletRequest request,@RequestBody NoticeDTO noticeDTO) {
        noticeDTO.setWriter("관리자");

        log.info(request.getHeader("Authorization").substring(7));
        String jwtToken = request.getHeader("Authorization");
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        String username = claims.getBody().getSubject();
        log.info(username);
        Notice persistNotice = (noticeService.save(noticeDTO));
        NoticeDTO saveNotice = persistNotice.ToDTO();
        return new ResponseEntity<>(saveNotice, HttpStatus.CREATED);
    }

    //공지 수정
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> putNotice(@PathVariable("id") Long id, @RequestBody NoticeDTO noticeDTO) {


        Notice persistNotice = noticeService.findNoticeById(id);

        NoticeDTO modifyNotice = persistNotice.ToDTO();

        modifyNotice.setContent(noticeDTO.getContent());
        modifyNotice.setTitle(noticeDTO.getTitle());


        NoticeDTO saveNotice = noticeService.save(modifyNotice).ToDTO();

        return new ResponseEntity<>(saveNotice, HttpStatus.OK);
    }

    //공지 삭제
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("id") Long id) {
        noticeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
