package com.ken.hspace.resource.config;

import com.ken.hspace.resource.handler.SimpleAccessDeniedHandler;
import com.ken.hspace.resource.point.SimpleAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启鉴权服务
public class ResourceServerConfig {
    @Bean
    public SecurityFilterChain httpSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 所有请求都进行拦截
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        // 关闭session
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        // 配置资源服务器的无权限，无认证拦截器等 以及JWT验证
        httpSecurity.oauth2ResourceServer()
                .accessDeniedHandler(new SimpleAccessDeniedHandler())
                .authenticationEntryPoint(new SimpleAuthenticationEntryPoint())
                .jwt();
        return httpSecurity.build();
    }
}
