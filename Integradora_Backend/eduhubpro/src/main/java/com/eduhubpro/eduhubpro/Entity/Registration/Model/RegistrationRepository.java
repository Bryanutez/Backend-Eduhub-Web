package com.eduhubpro.eduhubpro.Entity.Registration.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, UUID> {
    // Encontrar cursos del estudiante
    @Query("SELECT r FROM Registration r WHERE r.student.userId =:userId AND r.registrationStatus !=:status")
    List<Registration> findAllNotFinalizedCourses(@Param("userId") UUID userId, @Param("status") RegistrationStatus status);
}