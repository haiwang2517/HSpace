# HSpace
权限鉴权服务
启动准备:
* host 文件添加 127.0.0.1 haiyinlong.com
* 启动 authorization-server 是需要配置 `-Djasypt.encryptor.password=`

目前问题:
* client 登录后跳转 callback时提示 `[authorization_request_not_found]`