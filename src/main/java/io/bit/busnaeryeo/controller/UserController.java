package io.bit.busnaeryeo.controller;

import io.bit.busnaeryeo.domain.dto.LoginDTO;
import io.bit.busnaeryeo.domain.entity.User;
import io.bit.busnaeryeo.domain.dto.UserDTO;
import io.bit.busnaeryeo.jwt.JwtTokenProvider;
import io.bit.busnaeryeo.service.RedisServiceImpl;
import io.bit.busnaeryeo.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//    @Au
//    public UserController(JwtTokenProvider jwtTokenProvider, UserServiceImpl userService, RedisServiceImpl redisService){
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.userService = userService;
//        this.redisService = redisService;
//    }

    // 회원가입
    @PostMapping("/signUp")
    public ResponseEntity join(@RequestBody @Valid UserDTO userDTO) {
        Long result = userService.join(userDTO);
        return result != null ?
                ResponseEntity.ok().body("회원가입을 축하합니다!") :
                ResponseEntity.badRequest().build();
    }

    @PostMapping("/signUp/admin")
    public ResponseEntity joinAdmin(@RequestBody @Valid UserDTO userDTO) {
        Long result = userService.joinAdmin(userDTO);
        return result != null ?
                ResponseEntity.ok().body("회원가입을 축하합니다!") :
                ResponseEntity.badRequest().build();
    }

    @PostMapping("/signUp/driver")
    public ResponseEntity joinDriver(@RequestBody @Valid UserDTO userDTO) {
        Long result = userService.joinDriver(userDTO);
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
        userService.checkPassword(user, loginDTO);
        // 어세스, 리프레시 토큰 발급 및 헤더 설정
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRoles());
        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

        // Redis 인메모리에 리프레시 토큰 저장
        redisService.setValues(refreshToken, user.getUsername());
        // 리프레시 토큰 저장소에 저장
        ////tokenRepository.save(new RefreshToken(refreshToken));

        return ResponseEntity.ok().body("Login!");
    }

    // 로그아웃
    @GetMapping("/api/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        redisService.delValues(request.getHeader("refreshToken"));
        return ResponseEntity.ok().body("Logout!");
    }

    // JWT 인증 요청 테스트
    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        return "Hello, User?";
    }

}