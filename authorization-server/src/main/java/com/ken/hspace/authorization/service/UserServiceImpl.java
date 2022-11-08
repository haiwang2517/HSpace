package com.ken.hspace.authorization.service;

import com.ken.hspace.authorization.repository.AuthenticationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final AuthenticationUserRepository authenticationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(authenticationUserRepository.getUserByUsername(username)).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }
}
