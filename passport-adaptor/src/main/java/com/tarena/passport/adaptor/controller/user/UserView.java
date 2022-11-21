package com.tarena.passport.adaptor.controller.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Data
public class UserView {
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String phone;

    private String email;

    private Integer enable;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtModified;
}
