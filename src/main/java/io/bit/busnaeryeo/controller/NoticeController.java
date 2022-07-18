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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@Log4j2
public class NoticeController {
    @Value("${jwt.secretkey}")
    private String secretKey;
    private final NoticeServiceImpl noticeService;

//    public NoticeController(NoticeServiceImpl noticeService){
//        this.noticeService = noticeService;
//    }


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


        // jwt 토큰에서 username을 가져와서 작성자를 따로 입력하지 않아도 값이 들어가도록 함
        String jwtToken = request.getHeader("Authorization").substring(7);
        Jws<Claims> claims = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes())).parseClaimsJws(jwtToken);
        // 이줄을 고쳐야함 오류가 걸렸던 이유 signkey에 직접적으로 String을 값을 넣어줬는데 역파싱 하기 위해서는 secretkry를 byte로 파싱해준뒤 역파싱해야함.
        String username = claims.getBody().getSubject();

        noticeDTO.setWriter(username);

        Notice persistNotice = (noticeService.save(noticeDTO));
        NoticeDTO saveNotice = persistNotice.ToDTO();
        return new ResponseEntity<>(saveNotice, HttpStatus.CREATED);
    }

    //공지 수정
    @PatchMapping("/admin/{id}")
    public ResponseEntity<?> putNotice(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody NoticeDTO noticeDTO) {


        Notice persistNotice = noticeService.findNoticeById(id);

        NoticeDTO modifyNotice = persistNotice.ToDTO();

        // jwt 토큰에서 username을 가져와서 작성자를 따로 입력하지 않아도 값이 들어가도록 함
        String jwtToken = request.getHeader("Authorization").substring(7);
        Jws<Claims> claims = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes())).parseClaimsJws(jwtToken);
        // 이줄을 고쳐야함 오류가 걸렸던 이유 signkey에 직접적으로 String을 값을 넣어줬는데 역파싱 하기 위해서는 secretkry를 byte로 파싱해준뒤 역파싱해야함.
        String username = claims.getBody().getSubject();

        modifyNotice.setWriter(username);

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
