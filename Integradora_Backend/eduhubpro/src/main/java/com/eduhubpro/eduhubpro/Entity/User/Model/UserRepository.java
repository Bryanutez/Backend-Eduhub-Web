package com.eduhubpro.eduhubpro.Entity.User.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Encontrar usuarios por correo
    Optional<User> findByEmail(String email);

    // Se usa en el auth controller
    Optional<User> findByEmailAndPassword(String email, String password);

    // Se usa para el método finAll en el service para el rol admin
    @Query("SELECT u FROM User u WHERE u.role IN :roles")
    List<User> findAllByRole(@Param("roles") List<UserRole> roles);

    // Encontrar correos duplicados al actualizar
    @Query("SELECT u.email FROM User u WHERE u.email =:email AND u.userId <> :userId")
    Optional<String> findDuplicateEmail(@Param("email") String email, @Param("userId") UUID userId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role =:role")
    long countAllUsers(@Param("role") UserRole role);
}