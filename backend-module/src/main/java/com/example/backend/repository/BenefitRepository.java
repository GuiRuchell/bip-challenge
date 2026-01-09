package com.example.backend.repository;

import com.example.backend.entity.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenefitRepository extends JpaRepository<BenefitEntity, Long> {

    @Query("SELECT b FROM BenefitEntity b WHERE b.active = true ORDER BY b.name")
    List<BenefitEntity> findAllAtivos();

    @Query("SELECT b FROM BenefitEntity b WHERE b.id = :id AND b.active = true")
    Optional<BenefitEntity> findByIdAndActive(Long id);

    boolean existsByName(String name);

    @Query("SELECT COUNT(b) > 0 FROM BenefitEntity b WHERE b.name = :name AND b.id <> :id")
    boolean existsByNameAndIdNot(String name, Long id);

}
