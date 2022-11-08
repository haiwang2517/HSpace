package com.ken.hspace.authorization.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.ken.hspace.authorization.repository.entity.AuthenticationUser;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonDeserialize(using = JwtAuthenticationTokenDeserializer.class)
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtAuthenticationTokenMixin {}

class JwtAuthenticationTokenDeserializer extends JsonDeserializer<AuthenticationUser> {

  @Override
  public AuthenticationUser deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    ObjectMapper mapper = (ObjectMapper) parser.getCodec();
    JsonNode root = mapper.readTree(parser);
    return deserialize(parser, mapper, root);
  }

  private AuthenticationUser deserialize(JsonParser parser, ObjectMapper mapper, JsonNode root)
      throws JsonParseException {
    JsonNode idJsonNode = readJsonNode(root, "id");
    JsonNode usernameJsonNode = readJsonNode(root, "username");
    JsonNode emailJsonNode = readJsonNode(root, "email");
    JsonNode passwordJsonNode = readJsonNode(root, "password");
    AuthenticationUser user = new AuthenticationUser();
    user.setId(idJsonNode.textValue());
    user.setUsername(usernameJsonNode.textValue());
    user.setEmail(emailJsonNode.textValue());
    user.setPassword(passwordJsonNode.textValue());

    List<GrantedAuthority> authorities = null;
    try {
      authorities =
          mapper.readerForListOf(GrantedAuthority.class).readValue(root.get("authorities"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    user.setAuthorities(authorities);
    // 如果有JWT可以使用这个来解析
    //    JsonNode principal = readJsonNode(root, "principal");
    //    if (!Objects.isNull(principal)) {
    //      String tokenValue = principal.get("tokenValue").textValue();
    //      long issuedAt = principal.get("issuedAt").longValue();
    //      long expiresAt = principal.get("expiresAt").longValue();
    //      Map<String, Object> headers =
    //          JsonNodeUtils.findValue(principal, "headers", JsonNodeUtils.STRING_OBJECT_MAP,
    // mapper);
    //      Map<String, Object> claims = new HashMap<>();
    //      claims.put("claims", principal.get("claims"));
    //      Jwt jwt =
    //          new Jwt(
    //              tokenValue,
    //              Instant.ofEpochMilli(issuedAt),
    //              Instant.ofEpochMilli(expiresAt),
    //              headers,
    //              claims);
    //      return new AuthenticationUser();
    //    }
    return user;
  }

  private JsonNode readJsonNode(JsonNode jsonNode, String field) {
    return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
  }
}

abstract class JsonNodeUtils {

  static final TypeReference<Set<String>> STRING_SET = new TypeReference<Set<String>>() {};

  static final TypeReference<Map<String, Object>> STRING_OBJECT_MAP =
      new TypeReference<Map<String, Object>>() {};

  static String findStringValue(JsonNode jsonNode, String fieldName) {
    if (jsonNode == null) {
      return null;
    }
    JsonNode value = jsonNode.findValue(fieldName);
    return (value != null && value.isTextual()) ? value.asText() : null;
  }

  static <T> T findValue(
      JsonNode jsonNode,
      String fieldName,
      TypeReference<T> valueTypeReference,
      ObjectMapper mapper) {
    if (jsonNode == null) {
      return null;
    }
    JsonNode value = jsonNode.findValue(fieldName);
    return (value != null && value.isContainerNode())
        ? mapper.convertValue(value, valueTypeReference)
        : null;
  }

  static JsonNode findObjectNode(JsonNode jsonNode, String fieldName) {
    if (jsonNode == null) {
      return null;
    }
    JsonNode value = jsonNode.findValue(fieldName);
    return (value != null && value.isObject()) ? value : null;
  }
}
