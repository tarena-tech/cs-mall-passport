package com.tarena.passport.common.pojo.view;

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

    private Date gmtCreate;

    private Date gmtModified;
}
