package com.example.streak.work.model;

import com.example.streak.streak.db.StreakEntity;
import com.example.streak.work.db.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<WorkEntity, Long> {
    Optional<WorkEntity> findFirstByUserIdAndOrderNum(Long userId, Long orderNum);
}
