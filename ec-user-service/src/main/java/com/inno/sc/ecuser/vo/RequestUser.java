package com.inno.sc.ecuser.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {

    @NotNull(message = "email required")
    @Size(min = 2, message = "not less than 2")
    @Email
    private String email;

    @NotNull(message = "password required")
    @Size(min = 8, message = "not less than 8")
    private String pwd;

    @NotNull(message = "name required")
    @Size(min = 2, message = "not less than 2")
    private String name;
}
