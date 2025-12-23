package com.agricultural.management.config;

import com.agricultural.management.entity.User;
import com.agricultural.management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有用户数据
        if (userRepository.count() == 0) {
            // 创建初始用户数据
            createInitialUsers();
        }
    }

    private void createInitialUsers() {
        // 创建管理员用户
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRealName("管理员");
        admin.setEmail("admin@example.com");
        admin.setPhone("13800138001");
        admin.setRole(User.UserRole.ADMIN);
        admin.setStatus(true);

        // 创建农民用户
        User farmer = new User();
        farmer.setUsername("farmer1");
        farmer.setPassword(passwordEncoder.encode("farmer123"));
        farmer.setRealName("张三");
        farmer.setEmail("zhangsan@example.com");
        farmer.setPhone("13800138002");
        farmer.setRole(User.UserRole.FARMER);
        farmer.setStatus(true);

        // 创建工作人员用户
        User staff = new User();
        staff.setUsername("staff1");
        staff.setPassword(passwordEncoder.encode("staff123"));
        staff.setRealName("李四");
        staff.setEmail("lisi@example.com");
        staff.setPhone("13800138003");
        staff.setRole(User.UserRole.STAFF);
        staff.setStatus(true);

        userRepository.save(admin);
        userRepository.save(farmer);
        userRepository.save(staff);

        System.out.println("初始用户数据创建完成！");
        System.out.println("管理员账号: admin / admin123");
        System.out.println("农民账号: farmer1 / farmer123");
        System.out.println("工作人员账号: staff1 / staff123");
    }
}
