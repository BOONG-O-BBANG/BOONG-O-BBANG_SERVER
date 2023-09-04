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
        userNaverId = jwtUtils.extractUsername(accessToken);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userNaverId);

        //accessToken이 유효한 경우
        if (jwtUtils.isTokenValid(accessToken, userDetails)) {
            UserSignInDto dto = UserSignInDto.builder()
                    .userNaverId(userNaverId)
                    .build();

            refreshTokenRepository.saveRefreshToken(accessToken, refreshToken);

            RequestContextHolder.currentRequestAttributes()
                    .setAttribute(AUTHORIZATION, accessToken, RequestAttributes.SCOPE_REQUEST);
            RequestContextHolder.currentRequestAttributes()
                    .setAttribute("RefreshHeader", refreshToken, RequestAttributes.SCOPE_REQUEST);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, dto, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            String findRefreshToken = refreshTokenRepository.findRefreshToken(userNaverId);
            if (jwtUtils.isRefreshTokenValid(findRefreshToken)) {

                String newAccessToken = jwtUtils.generateToken(userDetails);

                RequestContextHolder.currentRequestAttributes()
                        .setAttribute(AUTHORIZATION, newAccessToken, RequestAttributes.SCOPE_REQUEST);
                RequestContextHolder.currentRequestAttributes()
                        .setAttribute("RefreshHeader", findRefreshToken, RequestAttributes.SCOPE_REQUEST);

                refreshTokenRepository.saveRefreshToken(userNaverId, findRefreshToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}