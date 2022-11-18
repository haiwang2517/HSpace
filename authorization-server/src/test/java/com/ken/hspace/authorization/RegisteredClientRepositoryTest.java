package com.ken.hspace.authorization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;

import java.time.Duration;
import java.util.UUID;

@SpringBootTest
public class RegisteredClientRepositoryTest {
  @Autowired RegisteredClientRepository registeredClientRepository;

  @Autowired PasswordEncoder passwordEncoder;

  @Test
  public void addClient() {
    RegisteredClient registeredClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("hspace-baidu")
            .clientSecret(passwordEncoder.encode("client_secret"))
                .clientAuthenticationMethods(s -> {
              s.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
              s.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
              s.add(ClientAuthenticationMethod.CLIENT_SECRET_JWT);
                })
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUris( url ->{
                  url.add("http://haiyinlong.com:8082/login/oauth2/code/hspace");
                  url.add("http://haiyinlong.com:8082/login/oauth2/code/messaging-client-model");
                  url.add("http://www.baidu.com");
                })
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .scope("message.read")
            .scope("message.write")
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofMinutes(10L))
                    .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                    .reuseRefreshTokens(true)
                    .refreshTokenTimeToLive(Duration.ofHours(3L))
                    .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                    .build())
            .build();

    registeredClientRepository.save(registeredClient);
  }
}
