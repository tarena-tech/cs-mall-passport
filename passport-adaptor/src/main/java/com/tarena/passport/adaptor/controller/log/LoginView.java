package com.tarena.passport.adaptor.controller.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
public class LoginView {
    private Long id;
    private String username;
    private String ip;
    private String userAgent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String gmtLogin;

}
