package com.inno.sc.ecuser.dto;

import com.inno.sc.ecuser.vo.ResponseOrder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String pwd;
    private String name;

    private String userId;
    private Date createdAt;
    private String encrypedPwd;

    private List<ResponseOrder> orders;

}
