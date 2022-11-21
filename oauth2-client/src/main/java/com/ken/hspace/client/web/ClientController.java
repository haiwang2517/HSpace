package com.ken.hspace.client.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ClientController {

    @GetMapping("/getUserInfo")
    public Object show(@AuthenticationPrincipal OAuth2User user, @AuthenticationPrincipal Jwt jwt) {
        return user;
    }
}
