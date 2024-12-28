package com.example.streak.streak.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StreakRepository extends JpaRepository<StreakEntity, Long> {
    Optional<StreakEntity> findFirstByWorkIdOrderByMonthDesc(Long workId);
    List<StreakEntity> findAllByWorkIdOrderByMonthDesc(Long workId);
}
