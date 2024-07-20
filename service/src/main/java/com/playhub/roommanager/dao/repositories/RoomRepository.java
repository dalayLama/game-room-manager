package com.playhub.roommanager.dao.repositories;

import com.playhub.roommanager.dao.entities.RoomEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT r FROM RoomEntity r where r.id = :id")
    Optional<RoomEntity> pessimisticWrite(@Param("id") UUID id);

}
