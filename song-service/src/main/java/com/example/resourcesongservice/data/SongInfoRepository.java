package com.example.resourcesongservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongInfoRepository extends JpaRepository<com.example.resourcesongservice.data.SongInfo, Long> {
    boolean existsByResourceId(Long resourceId);
}
