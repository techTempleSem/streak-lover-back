package com.example.streak.work.service;

import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.model.WorkDTO;
import org.springframework.stereotype.Service;

@Service
public class WorkConverter {
    public WorkDTO toDTO(WorkEntity workEntity){
        return WorkDTO.builder()
                .id(workEntity.getId())
                .name(workEntity.getName())
                .createdAt(workEntity.getCreatedAt())
                .orderNum(workEntity.getOrderNum())
                .lastUpdatedAt(workEntity.getLastUpdatedAt())
                .curStreak(workEntity.getCurStreak())
                .dayWeek(workEntity.getDayWeek())
                .money(workEntity.getMoney())
                .repair(workEntity.getRepair()).build();
    }
}
