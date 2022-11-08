package com.ken.hspace.authorization;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class AuthorizationServiceApplication {
  public static void main(String[] args) {

    SpringApplication.run(AuthorizationServiceApplication.class, args);
  }
}
