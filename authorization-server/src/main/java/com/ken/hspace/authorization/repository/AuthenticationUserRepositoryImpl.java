package com.ken.hspace.authorization.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ken.hspace.authorization.repository.dao.UserRepository;
import com.ken.hspace.authorization.repository.entity.AuthenticationUser;
import com.ken.hspace.authorization.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class AuthenticationUserRepositoryImpl implements AuthenticationUserRepository{
    private final UserRepository userRepository;
    @Override
    public AuthenticationUser getUserByUsername(String username) {
        UserEntity userEntity = userRepository.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username));
        if(null == userEntity){
            return null;
        }
        // 组装对象
        AuthenticationUser authenticationUser = new AuthenticationUser();
        authenticationUser.setId(userEntity.getId()+"");
        authenticationUser.setUsername(userEntity.getUsername());
        authenticationUser.setPassword(userEntity.getPassword());
        authenticationUser.setEmail(userEntity.getEmail());
        authenticationUser.setAuthorities(AuthorityUtils.createAuthorityList("USER", "ADMIN" ,"read", "write"));
        return authenticationUser;
    }
}
