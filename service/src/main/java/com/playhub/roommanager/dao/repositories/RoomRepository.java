package com.playhub.roommanager.dao.repositories;

import com.playhub.roommanager.dao.entities.RoomEntity;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT r FROM RoomEntity r where r.id = :id")
    Optional<RoomEntity> pessimisticWrite(@NotNull@Param("id") UUID id);

    @Query("DELETE FROM RoomEntity r WHERE r.id = :id")
    @Modifying(flushAutomatically = true)
    void deleteById(@NotNull @Param("id") UUID id);

}
