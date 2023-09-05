package com.project.boongobbang.jwt;

import com.project.boongobbang.domain.dto.user.UserSignInDto;
import com.project.boongobbang.repository.redis.RedisRefreshTokenRepository;
import com.project.boongobbang.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final RedisRefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String refreshToken = request.getHeader("RefreshToken");
        final String userNaverId;
        final String accessToken;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = authHeader.substring(7);

        if (jwtUtils.isTokenValid(accessToken)) {
            userNaverId = jwtUtils.extractUsername(accessToken);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userNaverId);

            UserSignInDto dto = UserSignInDto.builder()
                    .userNaverId(userNaverId)
                    .build();

            refreshTokenRepository.saveRefreshToken(userNaverId, refreshToken);

            RequestContextHolder.currentRequestAttributes()
                    .setAttribute(AUTHORIZATION, accessToken, RequestAttributes.SCOPE_REQUEST);
            RequestContextHolder.currentRequestAttributes()
                    .setAttribute("RefreshToken", refreshToken, RequestAttributes.SCOPE_REQUEST);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, dto, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
            return;
        }

        if (jwtUtils.isTokenValid(refreshToken)) {
            RequestContextHolder.currentRequestAttributes()
                    .setAttribute(AUTHORIZATION, null, RequestAttributes.SCOPE_REQUEST);
            RequestContextHolder.currentRequestAttributes()
                    .setAttribute("RefreshToken", refreshToken, RequestAttributes.SCOPE_REQUEST);

            filterChain.doFilter(request, response);
            return;
        }

        RequestContextHolder.currentRequestAttributes()
                .setAttribute(AUTHORIZATION, null, RequestAttributes.SCOPE_REQUEST);
        RequestContextHolder.currentRequestAttributes()
                .setAttribute("RefreshToken", null, RequestAttributes.SCOPE_REQUEST);
        filterChain.doFilter(request, response);
    }
}