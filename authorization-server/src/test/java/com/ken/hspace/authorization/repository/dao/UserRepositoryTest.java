package com.ken.hspace.authorization.repository.dao;

import com.ken.hspace.authorization.repository.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserRepositoryTest {

  @Autowired private UserRepository userRepository;
  @Autowired PasswordEncoder passwordEncoder;

  @Test
  public void addUser() {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("admin");
    userEntity.setPassword(passwordEncoder.encode("123"));
    userEntity.setEmail("xx@163.com");

    userRepository.insert(userEntity);
  }
}
