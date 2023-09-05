package com.project.boongobbang.service;

import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.exception.AppException;
import com.project.boongobbang.exception.ErrorCode;
import com.project.boongobbang.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String userNaverId) {
        User user = userRepository.findByUserNaverId(userNaverId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_FOUND, "[Error] 유저를 찾을 수 없습니다."));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new org.springframework.security.core.userdetails.User(
                user.getUserNaverId(),
                "",
                authorities
        );
    }
}

