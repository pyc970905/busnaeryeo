package io.bit.busnaeryeo.service;

import io.bit.busnaeryeo.domain.dto.LoginDTO;
import io.bit.busnaeryeo.domain.dto.SignUpDTO;
import io.bit.busnaeryeo.domain.entity.User;
import io.bit.busnaeryeo.domain.dto.UserDTO;
import io.bit.busnaeryeo.jwt.JwtTokenProvider;
import io.bit.busnaeryeo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisServiceImpl redisService;


    //일반 회원용 회원가입
    public Long join(SignUpDTO signUpDTO) {
        signUpDTO.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        Long userId = userRepository.save(signUpDTO.toEntity()).getId();

        return userId;

    }
    public Long joinAdmin(SignUpDTO signUpDTO) {
        signUpDTO.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        Long userId = userRepository.save(signUpDTO.toAdminEntity()).getId();

        return userId;

    }
    public Long joinDriver(SignUpDTO signUpDTO) {
        signUpDTO.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        Long userId = userRepository.save(signUpDTO.toDriverEntity()).getId();

        return userId;

    }


    public User findUser(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("없는 아이디 입니다."));
        return user;
    }

    public boolean checkPassword(User user, LoginDTO loginDTO) {

        boolean result = passwordEncoder.matches(loginDTO.getPassword(),user.getPassword());
        if(!result)
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        return true;
        //return passwordEncoder.matches(user.getPassword(), member.getPassword());
    }

    @Transactional
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String accToken = jwtTokenProvider.resolveAccessToken(request);
        System.out.println(accToken);
        // AccessToken 검증
        if(!jwtTokenProvider.validateToken(accToken)) {
            return  ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
        // AccessToken에서 이메일 가져오기.
        Authentication authentication = jwtTokenProvider.getAuthentication(accToken);

        //Redis에서 해당 username으로 저장된 refresh토큰이 있으면 삭제하고 액세스토큰 블랙리스트로 생성
        if(jwtTokenProvider.existsRefreshToken(authentication.getName())) {
            String key = authentication.getName() + "isLogout";
            System.out.println(key);
            redisService.delValues(authentication.getName());
            Long expiration = jwtTokenProvider.getExpireTime(accToken);
            System.out.println(expiration);
            System.out.println(accToken);
            redisService.setValuesWithExp(key, accToken, expiration);

        }
        return ResponseEntity.ok().body("Logout Complete.");

    }
}
