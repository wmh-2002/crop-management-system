package com.agricultural.management.service;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.User;
import com.agricultural.management.exception.ResourceNotFoundException;
import com.agricultural.management.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 分页查询用户列表
     */
    public PageResponse<UserResponse> getUserList(UserQueryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Specification<User> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("username"),
                    "%" + request.getUsername().trim() + "%"));
            }

            if (request.getRealName() != null && !request.getRealName().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("realName"),
                    "%" + request.getRealName().trim() + "%"));
            }

            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("email"),
                    "%" + request.getEmail().trim() + "%"));
            }

            if (request.getRole() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), request.getRole()));
            }

            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<User> userPage = userRepository.findAll(spec, pageable);

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(UserResponse::new)
                .toList();

        return new PageResponse<>(userResponses, request.getPage(), request.getSize(),
                userPage.getTotalElements(), userPage.getTotalPages(), userPage.isLast());
    }

    /**
     * 根据ID获取用户信息
     */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
        return new UserResponse(user);
    }

    /**
     * 创建新用户
     */
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty() &&
            userRepository.existsByEmail(request.getEmail().trim())) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName().trim());
        user.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        user.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));

        // 检查邮箱是否被其他用户使用
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty() &&
            !request.getEmail().trim().equals(user.getEmail()) &&
            userRepository.existsByEmail(request.getEmail().trim())) {
            throw new RuntimeException("邮箱已被其他用户使用");
        }

        // 更新字段
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        }

        if (request.getRealName() != null && !request.getRealName().trim().isEmpty()) {
            user.setRealName(request.getRealName().trim());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail().trim().isEmpty() ? null : request.getEmail().trim());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone().trim().isEmpty() ? null : request.getPhone().trim());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("用户不存在，ID: " + id);
        }

        // 检查是否为管理员用户，防止误删管理员
        User user = userRepository.findById(id).get();
        if (user.getRole() == User.UserRole.ADMIN && user.getUsername().equals("admin")) {
            throw new RuntimeException("不能删除系统管理员账户");
        }

        userRepository.deleteById(id);
    }

    /**
     * 启用/禁用用户
     */
    @Transactional
    public UserResponse updateUserStatus(Long id, Boolean status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));

        // 防止禁用管理员账户
        if (!status && user.getRole() == User.UserRole.ADMIN && user.getUsername().equals("admin")) {
            throw new RuntimeException("不能禁用系统管理员账户");
        }

        user.setStatus(status);
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }
}
