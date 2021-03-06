package com.santander.birrameet.security;

import com.santander.birrameet.security.utils.JWTUtils;
import com.santander.birrameet.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtils jwtUtils;
    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        try {
            String username = jwtUtils.getUsernameFromToken(authToken);
            if (!jwtUtils.validateToken(authToken)) {
                return Mono.empty();
            }
            Claims claims = jwtUtils.getAllClaimsFromToken(authToken);
            List<String> rolesMap = claims.get("role", List.class);
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String rolemap : rolesMap) {
                authorities.add(new SimpleGrantedAuthority(rolemap));
            }
            UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(username, null, authorities);
            return userService.findByUsername(username).map(user -> {
                token.setDetails(user);
                return token;
            });
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}