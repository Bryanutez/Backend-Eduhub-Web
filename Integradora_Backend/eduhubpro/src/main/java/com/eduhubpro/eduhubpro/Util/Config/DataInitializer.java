package com.eduhubpro.eduhubpro.Util.Config;

import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserRepository;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Usuario con rol de ADMIN
            Optional<User> optionalAdmin = userRepository.findByEmail("carsi@example.com");
            if (!optionalAdmin.isPresent()) {
                User adminUser = new User(
                        "Carsi Admin User",
                        "carsi@example.com",
                        passwordEncoder.encode("carsi123"),
                        UserRole.ADMIN
                );
                userRepository.saveAndFlush(adminUser);
            }

            // Usuario con rol de STUDENT
            Optional<User> optionalStudent = userRepository.findByEmail("student@example.com");
            if (!optionalStudent.isPresent()) {
                User studentUser = new User(
                        "Student User",
                        "student@example.com",
                        passwordEncoder.encode("student123"),
                        UserRole.STUDENT
                );
                userRepository.saveAndFlush(studentUser);
            }

            // Usuario con rol de INSTRUCTOR
            Optional<User> optionalInstructor = userRepository.findByEmail("instructor@example.com");
            if (!optionalInstructor.isPresent()) {
                User instructorUser = new User(
                        "Instructor User",
                        "instructor@example.com",
                        passwordEncoder.encode("instructor123"),
                        UserRole.INSTRUCTOR
                );
                userRepository.saveAndFlush(instructorUser);
            }

            // Usuario con rol de STUDENT
            Optional<User> optionalStudent2 = userRepository.findByEmail("student2@example.com");
            if (!optionalStudent2.isPresent()) {
                User studentUser = new User(
                        "Student User 2",
                        "student2@example.com",
                        passwordEncoder.encode("student123"),
                        UserRole.STUDENT
                );
                userRepository.saveAndFlush(studentUser);
            }

            // Usuario con rol de STUDENT
            Optional<User> optionalInstructor2 = userRepository.findByEmail("instructor2@example.com");
            if (!optionalInstructor2.isPresent()) {
                User instructorUser = new User(
                        "Instructor User 2",
                        "instructor2@example.com",
                        passwordEncoder.encode("instructor123"),
                        UserRole.INSTRUCTOR
                );
                userRepository.saveAndFlush(instructorUser);
            }

        };
    }

}