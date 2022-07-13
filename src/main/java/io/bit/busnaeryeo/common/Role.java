package io.bit.busnaeryeo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    DRIVER("ROLE_DRIVER");

    private String value;
}
