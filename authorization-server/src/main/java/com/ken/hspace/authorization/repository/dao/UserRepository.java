package com.ken.hspace.authorization.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ken.hspace.authorization.repository.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserRepository extends BaseMapper<UserEntity> {
}
