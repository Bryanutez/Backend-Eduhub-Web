package com.eduhubpro.eduhubpro.Entity.Section.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section, UUID> {

    // Buscar clases relacionadas a un módulo
    @Query("SELECT s FROM Section s WHERE s.module.moduleId =:moduleId AND s.status =:status")
    List<Section> findAllByModuleId(@Param("moduleId") UUID moduleId, @Param("status") Status status);
}
