package com.playhub.roommanager.dao.repositories;

import com.playhub.roommanager.dao.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {

}
