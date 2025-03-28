package com.eduhubpro.eduhubpro.Entity.Course.Model;

import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.CourseStatus;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    // Encontrar cursos asociados al docente cuando se cambia el estado de la cuenta
    @Query("SELECT c FROM Course c WHERE c.instructor.userId =:userId AND c.courseStatus !=:status")
    List<Course> findAllNotFinalizedCourses(@Param("userId") UUID userId, @Param("status") CourseStatus status);

    // Encontrar todos los cursos para el docente
    @Query("SELECT c FROM Course c WHERE c.instructor.userId =:userId")
    List<Course> findAllByInstructor(@Param("userId") UUID userId);

    // Encontrar todos los cursos publicados
    @Query("SELECT c FROM Course c WHERE c.courseStatus =:status")
    List<Course> findAllByStatus(@Param("status") CourseStatus status);

    // Encontrar cursor por docente
    @Query("SELECT c FROM Course c WHERE LOWER(c.instructor.name) LIKE LOWER(CONCAT('%', :instructorName, '%')) AND c.courseStatus = :status")
    List<Course> findByInstructorName(@Param("instructorName") String instructorName, @Param("status") CourseStatus status);

    // Encontrar cursos por fecha
    @Query("SELECT c FROM Course c WHERE c.startDate >= :startDate ")
    List<Course> findByDate(@Param("startDate") LocalDateTime startDate);

    // Contar cursos en curso y publicados
    @Query("SELECT COUNT(c) FROM Course c WHERE c.courseStatus IN :status")
    Long countCourses(@Param("status") List<CourseStatus> status);

}