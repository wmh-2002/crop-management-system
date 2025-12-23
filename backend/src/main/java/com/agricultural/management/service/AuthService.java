package com.agricultural.management.service;

import com.agricultural.management.dto.LoginRequest;
import com.agricultural.management.dto.LoginResponse;
import com.agricultural.management.entity.User;
import com.agricultural.management.repository.UserRepository;
import com.agricultural.management.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                      PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        // 使用Spring Security进行认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成真实的JWT token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 从数据库获取用户信息
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        LoginResponse.LoginData loginData = new LoginResponse.LoginData(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                user.getRole()
        );

        return new LoginResponse(200, "登录成功", loginData);
    }

    public void registerUser(LoginRequest registerRequest) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (registerRequest.getUsername().contains("@") &&
            userRepository.existsByEmail(registerRequest.getUsername())) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setRealName(registerRequest.getUsername()); // 默认真实姓名为用户名，可后续修改
        user.setRole(User.UserRole.FARMER); // 默认角色为农民
        user.setStatus(true);

        userRepository.save(user);
    }
}
