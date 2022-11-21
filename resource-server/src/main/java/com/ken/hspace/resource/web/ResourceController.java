package com.ken.hspace.resource.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ResourceController {
    @PreAuthorize("hasAuthority('SCOPE_all')")
    @GetMapping("/r")
    public String getResource(){
        return "获得到资源信息";
    }

    @GetMapping("/u")
    public String getResourceUser(@AuthenticationPrincipal Jwt principal){
        return principal.toString();
    }

    @GetMapping("/user")
    public String getResourceUser(Principal principal){
        return principal.toString();
    }

    /**
     * 默认登录成功跳转页为 /  防止404状态
     *
     * @return the map
     */
    @GetMapping
    public String index() {
        return "登录成功,默认访问资源";
    }
}
