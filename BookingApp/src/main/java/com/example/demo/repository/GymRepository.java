package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Gym;

public interface GymRepository extends JpaRepository<Gym, String> {
    List<Gym> findAll();
}
