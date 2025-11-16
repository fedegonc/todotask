package com.example.testddd.portal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortalTileRepository extends JpaRepository<PortalTile, Long> {

    List<PortalTile> findAllByEnabledTrueOrderByPositionAsc();

    List<PortalTile> findAllByOrderByPositionAsc();

    boolean existsByTitleIgnoreCase(String title);
}
