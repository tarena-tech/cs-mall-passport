package com.tarena.passport.common.pojo.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class UserQuery {

    private String username;

    private String password;

    private String nickname;

    private String phone;

    private String email;

    private Integer enable;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;

    private Date gmtModified;
}
