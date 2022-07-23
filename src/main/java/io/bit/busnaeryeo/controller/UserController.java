package io.bit.busnaeryeo.controller;

import io.bit.busnaeryeo.domain.dto.LoginDTO;
import io.bit.busnaeryeo.domain.dto.SignUpDTO;
import io.bit.busnaeryeo.domain.entity.User;
import io.bit.busnaeryeo.domain.dto.UserDTO;
import io.bit.busnaeryeo.jwt.JwtTokenProvider;
import io.bit.busnaeryeo.service.RedisServiceImpl;
import io.bit.busnaeryeo.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid SignUpDTO signUpDTO) {
        Long result = userService.join(signUpDTO);
        return result != null ?
                ResponseEntity.ok().body("회원가입을 축하합니다!") :
                ResponseEntity.badRequest().build();
    }

    @PostMapping("/join/admin")
    public ResponseEntity joinAdmin(@RequestBody @Valid SignUpDTO signUpDTO) {
        Long result = userService.joinAdmin(signUpDTO);
        return result != null ?
                ResponseEntity.ok().body("회원가입을 축하합니다!") :
                ResponseEntity.badRequest().build();
    }

    @PostMapping("/join/driver")
    public ResponseEntity joinDriver(@RequestBody @Valid SignUpDTO signUpDTO) {
        Long result = userService.joinDriver(signUpDTO);
        return result != null ?
                ResponseEntity.ok().body("회원가입을 축하합니다!") :
                ResponseEntity.badRequest().build();
    }

    // 로그인
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse response) {
        return userService.login(loginDTO, response);

    }

    // 로그아웃
    @GetMapping("/api/logout")
    public ResponseEntity logout(HttpServletRequest request) {

        return userService.logout(request);
    }

}