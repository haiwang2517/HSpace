package com.ken.hspace.client.callback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthorizationCodeCallBack {

    @GetMapping("/show")
    public Object show(HttpServletRequest request) {
        return "打开show接口";
    }
}
