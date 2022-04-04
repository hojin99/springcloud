package com.inno.sc.ecuser.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inno.sc.ecuser.dto.UserDto;
import com.inno.sc.ecuser.service.UserService;
import com.inno.sc.ecuser.vo.RequestLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    // 인증 처리
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws AuthenticationException {

        try {
            // Post로 들어오는 경우 request.getInputStream()으로 받아줘야 함
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // 인증 처리
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>() // 권한
                    )
            );

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response, 
                                            FilterChain chain, 
                                            Authentication authResult) throws IOException, ServletException {
        // 로그인 성공을 했을 때 처리
        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);

    }
}
