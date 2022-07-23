package io.bit.busnaeryeo.controller;

import io.bit.busnaeryeo.domain.dto.NoticeDTO;
import io.bit.busnaeryeo.domain.entity.Notice;
import io.bit.busnaeryeo.jwt.JwtTokenProvider;
import io.bit.busnaeryeo.service.NoticeServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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


import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@Log4j2
public class NoticeController {
    @Value("${jwt.secretkey}")
    private String secretKey;
    private final NoticeServiceImpl noticeService;
    private JwtTokenProvider jwtTokenProvider;




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
        return new ResponseEntity<>(noticeService.save(noticeDTO, request).toDTO(), HttpStatus.CREATED);
    }

    //공지 수정
    @PatchMapping("/admin/{id}")
    public ResponseEntity<?> putNotice(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody NoticeDTO noticeDTO) {
        //게시판 공지 수정 본인확인 메소드 추가
        NoticeDTO modifyNotice = noticeService.findNoticeById(id).toDTO();

        return noticeService.checkAuth(request, modifyNotice) ?
                ResponseEntity.ok().body(noticeService.save(modifyNotice, request).toDTO()) :
                ResponseEntity.badRequest().body("본인이 작성한 글이 아닙니다.");
    }

    //공지 삭제
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("id") Long id,HttpServletRequest request) {

        return noticeService.checkAuthById(request, id) ?
                ResponseEntity.ok().body(noticeService.deleteById(id)) :
                ResponseEntity.badRequest().body("본인이 작성한 글이 아닙니다.");
    }



}
