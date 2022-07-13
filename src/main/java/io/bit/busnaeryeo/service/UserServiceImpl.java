package io.bit.busnaeryeo.service;

import io.bit.busnaeryeo.domain.LoginDTO;
import io.bit.busnaeryeo.domain.User;
import io.bit.busnaeryeo.domain.UserDTO;
import io.bit.busnaeryeo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Long userId = userRepository.save(userDTO.toEntity()).getId();

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
}
