package io.bit.busnaeryeo.domain.dto;

import io.bit.busnaeryeo.domain.etity.User;
import lombok.Builder;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class UserDTO {

    // 토큰 발급 및 인증 학습이 목적이기 때문에
    // 검증 조건은 일단 생략..
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String gender;
    private String address;
    private String birthDay;
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();



    public User toEntity() {
        User user = User.builder()
                .id(id)
                .username(username)
                .password(password)
                .realName(realName)
                .gender(gender)
                .address(address)
                .birthDay(birthDay)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        return user;
    }
    public User toAdminEntity() {
        User user = User.builder()
                .id(id)
                .username(username)
                .password(password)
                .realName(realName)
                .gender(gender)
                .address(address)
                .birthDay(birthDay)
                .roles(Collections.singletonList("ROLE_ADMIN"))
                .build();
        return user;
    }
}