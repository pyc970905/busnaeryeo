package io.bit.busnaeryeo.controller;

import io.bit.busnaeryeo.domain.dto.LoginDTO;
import io.bit.busnaeryeo.domain.etity.User;
import io.bit.busnaeryeo.domain.dto.UserDTO;
import io.bit.busnaeryeo.jwt.JwtTokenProvider;
import io.bit.busnaeryeo.service.RedisServiceImpl;
import io.bit.busnaeryeo.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceImpl userService;
    private final RedisServiceImpl redisService;
    ////private final TokenRepository tokenRepository;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid UserDTO userDTO) {
        Long result = userService.join(userDTO);
        return result != null ?
                ResponseEntity.ok().body("회원가입을 축하합니다!") :
                ResponseEntity.badRequest().build();
    }

    @PostMapping("/join/admin")
    public ResponseEntity joinAdmin(@RequestBody @Valid UserDTO userDTO) {
        Long result = userService.join(userDTO);
        return result != null ?
                ResponseEntity.ok().body("회원가입을 축하합니다!") :
                ResponseEntity.badRequest().build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse response) {
        // 유저 존재 확인
        User user = userService.findUser(loginDTO);
        // 비밀번호 체크
        userService.checkPassword(user, loginDTO );
        // 어세스, 리프레시 토큰 발급 및 헤더 설정
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRoles());
        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

        // Redis 인메모리에 리프레시 토큰 저장
        redisService.setValues(refreshToken, user.getUsername());
        // 리프레시 토큰 저장소에 저장
        ////tokenRepository.save(new RefreshToken(refreshToken));

        return ResponseEntity.ok().body("로그인 성공!");
    }

    // 로그아웃
    @GetMapping("/api/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        redisService.delValues(request.getHeader("refreshToken"));
        return ResponseEntity.ok().body("로그아웃 성공!");
    }

    // JWT 인증 요청 테스트
    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        return "Hello, User?";
    }

}