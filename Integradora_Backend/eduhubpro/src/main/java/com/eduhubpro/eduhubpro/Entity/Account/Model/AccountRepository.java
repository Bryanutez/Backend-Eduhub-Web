package com.eduhubpro.eduhubpro.Entity.Account.Model;

import com.eduhubpro.eduhubpro.Entity.Category.Model.Category;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query("SELECT a FROM Account a WHERE a.status IN :status")
    List<Account> findAllByStatusActive(@Param("status") Status status);
}
