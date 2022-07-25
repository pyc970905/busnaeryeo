package io.bit.busnaeryeo.domain.dto;

import io.bit.busnaeryeo.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Collections;

@Getter
@Setter
public class SignUpDTO {
    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    private String realName;
    private String gender;
    private String address;
    private String birthDay;


    public User toEntity(){
        User user = User.builder()
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

    public User toDriverEntity() {
        User user = User.builder()
                .username(username)
                .password(password)
                .realName(realName)
                .gender(gender)
                .address(address)
                .birthDay(birthDay)
                .roles(Collections.singletonList("ROLE_DRIVER"))
                .build();
        return user;
    }

    public User toAdminEntity() {
        User user = User.builder()
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


