package com.marcbouchez.main.repository;

import com.marcbouchez.main.domain.Plateforme;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Plateforme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlateformeRepository extends JpaRepository<Plateforme, Long> {
}
