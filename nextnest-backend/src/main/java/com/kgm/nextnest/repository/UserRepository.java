package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.AccountStatus;
import com.kgm.nextnest.model.RoleType;
import com.kgm.nextnest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<User> findByAccountStatus(AccountStatus accountStatus);

    long countByRole(RoleType role);
}