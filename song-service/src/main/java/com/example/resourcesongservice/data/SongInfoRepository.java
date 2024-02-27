package com.example.resourcesongservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongInfoRepository extends JpaRepository<SongInfo, Long> {
    boolean existsByResourceId(Long resourceId);

    void removeByResourceId(Long resourceId);
}
