package com.ken.hspace.authorization;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthorizationServiceApplicationTest {

  private String value = "123";

  @Autowired private StringEncryptor stringEncryptor;

  @Test
  public void executeEncrypt() {
    // --jasypt.encryptor.password
    String encrypt = stringEncryptor.encrypt(value);
    System.out.println("加密后:" + encrypt);
    String decrypt = stringEncryptor.decrypt(encrypt);
    System.out.println("解密后:" + decrypt);
  }

  @Test
  public void localEncrypt() {
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
    // 算法类型
    config.setAlgorithm("PBEWithMD5AndDES");
    // 生成秘钥的公钥
    config.setPassword("123");
    // 应用配置
    encryptor.setConfig(config);
    // 加密
    System.out.println(encryptor.encrypt(value));
  }
}
