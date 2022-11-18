package com.ken.hspace.authorization.config;

import com.ken.hspace.authorization.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 配置<br>
 * https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserServiceImpl userServiceImpl;
  /**
   * 定义用户密码加密方式
   *
   * @return
   */
  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 自定义登录界面
   *
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  @Order(2)
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            (authorize) ->
                authorize
                    .antMatchers("/oauth2/**", "/userinfo", "/favicon.ico", "/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(form -> form.loginPage("/login").failureForwardUrl("/toError"))
        .userDetailsService(userServiceImpl)
        .csrf()
        .disable();

    return http.build();
  }
}
