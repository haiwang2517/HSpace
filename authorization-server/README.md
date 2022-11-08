##搭建步骤
> 官方地址：https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
>
csdn参考地址: https://blog.csdn.net/qq_37182370/article/details/124822587?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2-124822587-blog-125154592.pc_relevant_layerdownloadsortv1&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2-124822587-blog-125154592.pc_relevant_layerdownloadsortv1&utm_relevant_index=3
> https://blog.csdn.net/m0_67645544/article/details/125154592?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-4-125154592-blog-125133405.pc_relevant_aa&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-4-125154592-blog-125133405.pc_relevant_aa&utm_relevant_index=5        
> OAuth 2.0 的四种方式 https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html

**Oauth2.0 不支持password 模式**

依赖包

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.security</groupId>
<artifactId>spring-security-oauth2-authorization-server</artifactId>
<version>0.3.1</version>
</dependency>
```

```shell script
#请求url获取Code - GET
http://127.0.0.1:8080/oauth2/authorize?client_id=client_id&response_type=code&scope=all&redirect_uri=http://www.baidu.com

#获取token -POST
curl --location --request POST 'http://localhost:8080/oauth2/token' \
--header 'Authorization: Basic Y2xpZW50X2lkOmNsaWVudF9zZWNyZXQ=' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'code=7G2K-Q8sGvMrTkfF94cfFDjT5MEDveKhfjDhzJNTHSuONj1OwsbShxWDXV7rkHslGwiP5xeE-q6LFy3JxBlgUgkXrYDq5g44_Qd1ffbQ8XomYpUGoP_R-VlK1xODsBNe' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'scope=all' \
--data-urlencode 'redirect_uri=http://www.baidu.com'
```

* 使用数据库配置替换内存配置

git script

```shell script
eval `ssh-agent -s`
ssh-add ~/.ssh/id_ed25519_github
```

```sql
#数据库建表脚本
-- hspace.oauth2_authorization definition

CREATE TABLE `oauth2_authorization`
(
    `id`                            varchar(100) NOT NULL,
    `registered_client_id`          varchar(100) NOT NULL,
    `principal_name`                varchar(200) NOT NULL,
    `authorization_grant_type`      varchar(100) NOT NULL,
    `attributes`                    blob,
    `state`                         varchar(500)  DEFAULT NULL,
    `authorization_code_value`      blob,
    `authorization_code_issued_at`  datetime      DEFAULT NULL,
    `authorization_code_expires_at` datetime      DEFAULT NULL,
    `authorization_code_metadata`   blob,
    `access_token_value`            blob,
    `access_token_issued_at`        datetime      DEFAULT NULL,
    `access_token_expires_at`       datetime      DEFAULT NULL,
    `access_token_metadata`         blob,
    `access_token_type`             varchar(100)  DEFAULT NULL,
    `access_token_scopes`           varchar(1000) DEFAULT NULL,
    `oidc_id_token_value`           blob,
    `oidc_id_token_issued_at`       datetime      DEFAULT NULL,
    `oidc_id_token_expires_at`      datetime      DEFAULT NULL,
    `oidc_id_token_metadata`        blob,
    `refresh_token_value`           blob,
    `refresh_token_issued_at`       datetime      DEFAULT NULL,
    `refresh_token_expires_at`      datetime      DEFAULT NULL,
    `refresh_token_metadata`        blob,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- hspace.oauth2_authorization_consent definition

CREATE TABLE `oauth2_authorization_consent`
(
    `registered_client_id` varchar(100)  NOT NULL,
    `principal_name`       varchar(200)  NOT NULL,
    `authorities`          varchar(1000) NOT NULL,
    PRIMARY KEY (`registered_client_id`, `principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- hspace.oauth2_registered_client definition

CREATE TABLE `oauth2_registered_client`
(
    `id`                            varchar(100)  NOT NULL,
    `client_id`                     varchar(100)  NOT NULL,
    `client_id_issued_at`           datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `client_secret`                 varchar(200)           DEFAULT NULL,
    `client_secret_expires_at`      datetime               DEFAULT NULL,
    `client_name`                   varchar(200)  NOT NULL,
    `client_authentication_methods` varchar(1000) NOT NULL,
    `authorization_grant_types`     varchar(1000) NOT NULL,
    `redirect_uris`                 varchar(1000)          DEFAULT NULL,
    `scopes`                        varchar(1000) NOT NULL,
    `client_settings`               varchar(2000) NOT NULL,
    `token_settings`                varchar(2000) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- hspace.`user` definition

CREATE TABLE `user`
(
    `id`       bigint(20) NOT NULL AUTO_INCREMENT,
    `username` varchar(100) NOT NULL,
    `password` varchar(200) NOT NULL,
    `email`    varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';
```