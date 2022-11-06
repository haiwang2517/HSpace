package com.ken.hspace.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 配置<br>
 * https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfig {

  /**
   * 定义密码加密方式
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
                    .antMatchers("/oauth2/**", "/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(form -> form.loginPage("/login").failureForwardUrl("/toError"))
        .csrf()
        .disable();

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    String encode = getPasswordEncoder().encode("123");
    UserDetails user = User.withUsername("user").password(encode).roles("USER").build();
    UserDetails userDetails =
        User.withUsername("admin").password(encode).roles("USER", "ADMIN").build();

    return new InMemoryUserDetailsManager(user, userDetails);
  }
}
