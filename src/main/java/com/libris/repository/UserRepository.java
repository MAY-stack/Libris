package com.libris.repository;
import com.libris.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);  // 이메일로 사용자 찾기
    boolean existsByEmail(String email);  // 이메일 존재 여부 체크
    Optional<User> findByEmailVerificationToken(String token);
}
