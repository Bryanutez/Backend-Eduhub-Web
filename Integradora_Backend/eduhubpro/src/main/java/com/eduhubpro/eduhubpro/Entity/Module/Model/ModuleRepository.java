package com.eduhubpro.eduhubpro.Entity.Module.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.ModuleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {

    // Buscar módulos relacionadas a un curso
    @Query("SELECT m FROM Module m WHERE m.course.courseId =:courseId AND m.status IN :status")
    List<Module> findAllByCourseId(@Param("courseId") UUID moduleId, @Param("status") List<ModuleStatus> status);
}
