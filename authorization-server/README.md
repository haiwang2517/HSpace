##搭建步骤
> 官方地址：https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html    
> csdn参考地址: https://blog.csdn.net/qq_37182370/article/details/124822587?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2-124822587-blog-125154592.pc_relevant_layerdownloadsortv1&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2-124822587-blog-125154592.pc_relevant_layerdownloadsortv1&utm_relevant_index=3    
> https://blog.csdn.net/m0_67645544/article/details/125154592?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-4-125154592-blog-125133405.pc_relevant_aa&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-4-125154592-blog-125133405.pc_relevant_aa&utm_relevant_index=5        
>  OAuth 2.0 的四种方式 https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html

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

请求url
```shell script
http://127.0.0.1:8080/oauth2/authorize?client_id=client_id&response_type=code&scope=all&redirect_uri=http://www.baidu.com
```

* 使用数据库配置替换内存配置
* 使用资源服务器


git script
```shell script
eval `ssh-agent -s`
ssh-add ~/.ssh/id_ed25519_github
```

