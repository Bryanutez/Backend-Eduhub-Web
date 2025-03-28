package com.eduhubpro.eduhubpro.Entity.Category.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    // Para métodos fin all activos
    @Query("SELECT c FROM Category c WHERE c.status IN :status")
    List<Category> findAllByStatusActive(@Param("status") Status status);
}