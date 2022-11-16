package com.ken.hspace.client.callback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationCodeCallBack {


    @GetMapping("")
    public Object get() {
        return "xxxx";
    }
}
