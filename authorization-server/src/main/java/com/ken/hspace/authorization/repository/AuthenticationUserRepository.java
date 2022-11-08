package com.ken.hspace.authorization.repository;

import com.ken.hspace.authorization.repository.entity.AuthenticationUser;

public interface AuthenticationUserRepository {
    AuthenticationUser getUserByUsername(String username);
}
