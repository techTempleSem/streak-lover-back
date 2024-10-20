package com.example.streak.work.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkRepository extends JpaRepository<WorkEntity, Long> {
    Optional<WorkEntity> findFirstByUserIdAndOrderNum(Long userId, Long orderNum);
}
