package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.model.GymEquipment;
import com.example.demo.repository.GymEquipmentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymEquipmentService {

    private final GymEquipmentMapper gymEquipmentMapper;

    // 既存のfindAll()
    public List<GymEquipment> findAll() {
        return gymEquipmentMapper.findAll();
    }

    // 利用状況も取得するメソッドを追加
    public List<GymEquipment> findCurrentStatus() {
        return gymEquipmentMapper.findCurrentGymEquipmentStatus();
    }
    public GymEquipment findById(Integer id) {
        return gymEquipmentMapper.findById(id);
    }

    public boolean updateUseStatus(Integer id, boolean isInUse) {
        return gymEquipmentMapper.updateUseStatus(id, isInUse) > 0;
    }

    
}
