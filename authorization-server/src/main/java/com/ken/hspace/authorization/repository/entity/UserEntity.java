package com.ken.hspace.authorization.repository.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user")
public class UserEntity implements Serializable {
    @TableId
    private Long id;

    private String username;

    private String password;

    private String email;
}
