package com.libris.service;

import com.libris.entity.User;
import com.libris.repository.UserRepository;
import com.libris.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService; // 이메일 발송 서비스

    //회원가입
    public void registerUser(User user) {
        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 이메일 인증 토큰 생성
        String token = UUID.randomUUID().toString();
        user.setEmailVerificationToken(token);

        // 사용자 저장 (비활성화 상태)
        userRepository.save(user);

        // 이메일 발송
        String verificationLink = "http://your-domain.com/api/auth/verify-email?token=" + token;
        emailService.sendEmail(user.getEmail(), "Email Verification",
                "Click the link to verify your email: " + verificationLink);
    }

    public void verifyEmail(String token) {
        // 토큰으로 사용자 검색
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        // 계정 활성화
        user.setEnabled(true);
        user.setEmailVerificationToken(null); // 토큰 제거
        userRepository.save(user);
    }

    // 로그인 시 사용자 인증
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }
}