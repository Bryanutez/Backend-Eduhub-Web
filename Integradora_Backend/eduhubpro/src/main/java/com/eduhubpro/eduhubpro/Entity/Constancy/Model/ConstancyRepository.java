package com.eduhubpro.eduhubpro.Entity.Constancy.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConstancyRepository extends JpaRepository<Constancy, UUID> {

}
