package io.bit.busnaeryeo.service;

import io.bit.busnaeryeo.domain.dto.NoticeDTO;
import io.bit.busnaeryeo.domain.dto.UserDTO;
import io.bit.busnaeryeo.domain.entity.Notice;
import io.bit.busnaeryeo.domain.entity.User;
import io.bit.busnaeryeo.jwt.JwtTokenProvider;
import io.bit.busnaeryeo.repository.NoticeRepository;
import io.bit.busnaeryeo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${jwt.secretkey}")
    private String secretKey;

    //@Requestparam 으로 pageble이라는 숫자가 들어가고 return을 받아야 할 것
    public Page<Notice>findAll(Pageable pageable) {

        return noticeRepository.findAll(pageable);
    }

    public Notice findNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(new Notice());
        return notice;
    }

    public Notice save(NoticeDTO noticeDTO, HttpServletRequest request) {
        // jwt 토큰에서 username을 가져와서 작성자를 따로 입력하지 않아도 값이 들어가도록 함
        String username = jwtTokenProvider.resolveAccessToken(request);
        User user = userRepository.findByUsername(username).get();
        noticeDTO.setUser(user);
        noticeDTO.setWriter(user.getUsername());
        Notice saveNotice = noticeRepository.save(noticeDTO.toEntity());
        return saveNotice;
    }


    public boolean checkAuth(HttpServletRequest request, NoticeDTO noticeDTO) {
        String accessToken= jwtTokenProvider.resolveAccessToken(request);
        Jws<Claims> claims = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes())).parseClaimsJws(accessToken);
        // 이줄을 고쳐야함 오류가 걸렸던 이유 signkey에 직접적으로 String을 값을 넣어줬는데 역파싱 하기 위해서는 secretkry를 byte로 파싱해준뒤 역파싱해야함.
        String username = claims.getBody().getSubject();
        return username.equals(noticeDTO.getWriter());
    }

    public boolean checkAuthById(HttpServletRequest request, Long id) {
        String accessToken= jwtTokenProvider.resolveAccessToken(request);
        Jws<Claims> claims = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes())).parseClaimsJws(accessToken);
        // 이줄을 고쳐야함 오류가 걸렸던 이유 signkey에 직접적으로 String을 값을 넣어줬는데 역파싱 하기 위해서는 secretkry를 byte로 파싱해준뒤 역파싱해야함.
        String username = claims.getBody().getSubject();

        NoticeDTO noticeDTO= noticeRepository.findById(id).get().toDTO();
        return username.equals(noticeDTO.getWriter());
    }

    public Notice modify(NoticeDTO noticeDTO, HttpServletRequest request) {
        // jwt 토큰에서 username을 가져와서 작성자를 따로 입력하지 않아도 값이 들어가도록 함
        String jwtToken = request.getHeader("Authorization").substring(7);
        Jws<Claims> claims = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes())).parseClaimsJws(jwtToken);
        // 이줄을 고쳐야함 오류가 걸렸던 이유 signkey에 직접적으로 String을 값을 넣어줬는데 역파싱 하기 위해서는 secretkry를 byte로 파싱해준뒤 역파싱해야함.

        noticeDTO.setContent(noticeDTO.getContent());
        noticeDTO.setTitle(noticeDTO.getTitle());
        Notice saveNotice = noticeRepository.save(noticeDTO.toEntity());
        return saveNotice;
    }


    public ResponseEntity<?> deleteById(Long id) {
        noticeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
