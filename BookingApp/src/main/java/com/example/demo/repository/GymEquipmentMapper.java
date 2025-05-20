package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.model.GymEquipment;

@Mapper
public interface GymEquipmentMapper {

    /**
     * 今現在のジム機器利用状況を取得する
     * @return ジム機器の利用状況リスト
     */
    List<GymEquipment> findCurrentGymEquipmentStatus();

	List<GymEquipment> findAll();
	
	GymEquipment findById(Integer id);

	int updateUseStatus(@Param("id") Integer id, @Param("isInUse") boolean isInUse);

}
