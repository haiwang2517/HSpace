package com.ken.hspace.client.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class ClientController {

    /**
     * 页面默认打开该路径,然后当前用户信息及TOKEN信息
     * @param user
     * @return
     */
    @GetMapping("/")
    public String success(@RequestParam(value = "FROM",required = false,defaultValue = "") String from, @AuthenticationPrincipal OAuth2User user) {
        log.info("认证成功:{}", user);
        return "index";
    }

    @GetMapping("/getUserInfo")
    public Object getUserInfoAndToken(@AuthenticationPrincipal OAuth2User user) {
        log.info("认证成功:{}", user);
        // TODO 转化为VO
        return user;
    }
}
