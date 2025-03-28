package com.eduhubpro.eduhubpro.Entity.User.Service;

import com.eduhubpro.eduhubpro.Entity.Course.Model.Course;
import com.eduhubpro.eduhubpro.Entity.Course.Model.CourseRepository;
import com.eduhubpro.eduhubpro.Entity.Registration.Model.Registration;
import com.eduhubpro.eduhubpro.Entity.Registration.Model.RegistrationRepository;
import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserDto;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserRepository;
import com.eduhubpro.eduhubpro.Security.Jwt.JwtUtil;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.CourseStatus;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.RegistrationStatus;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.UserRole;
import com.eduhubpro.eduhubpro.Util.Enum.TypesResponse;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CourseRepository courseRepository, RegistrationRepository registrationRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
        this.jwtUtil = jwtUtil;
    }

    // --------------------------------------------
    // ADMIN
    // Analíticas
    @Transactional(readOnly = true)
    public ResponseEntity<Message> countAllUsers() {
        Long totalStudents = userRepository.countAllUsers(UserRole.STUDENT);
        Long totalInstructors = userRepository.countAllUsers(UserRole.STUDENT);
        Long totalCourses = courseRepository.countCourses(Arrays.asList(CourseStatus.PUBLISHED, CourseStatus.IN_PROGRESS));

        Map<String, Long> analytics = new HashMap<>();
        analytics.put("totalStudents", totalStudents);
        analytics.put("totalInstructors", totalInstructors);
        analytics.put("totalCourses", totalCourses);

        logger.info("Conteo de usuarios realizada correctamente");
        return new ResponseEntity<>(new Message(analytics, "Conteo de usuarios y cursos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // Consultar todos los usuarios
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllForAdmin() {
        List<User> users = userRepository.findAllByRole(Arrays.asList(UserRole.INSTRUCTOR, UserRole.STUDENT));
        logger.info("La búsqueda de usuarios ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(users, "Listado de usuarios", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Para consultar perfiles por el admin
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(UserDto dto) {
        UUID uuid = UUID.fromString(dto.getUserId());
        Optional<User> optionalUser = userRepository.findById(uuid);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la información del perfil", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        logger.info("La búsqueda del usuario ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(user, "Información del perfil encontrada exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Crear usuarios por el admin o registrarse
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> createUser(UserDto dto) {
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        if (optionalUser.isPresent()) {
            return new ResponseEntity<>(new Message("El correo electrónico proporcionado ya está en uso por otro usuario", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        if (UserRole.valueOf(dto.getRole()).equals(UserRole.ADMIN)) {
            return new ResponseEntity<>(new Message("El rol del usuario no puede ser administrador", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String hashPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User(dto.getName(), dto.getEmail(), hashPassword, UserRole.valueOf(dto.getRole()));

        user = userRepository.saveAndFlush(user);

        if (user == null) {
            return new ResponseEntity<>(new Message("No se pudo registrar la información", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("El registro ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(user, "Usuario registrado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // -------------------------------------------- todo implementar el servicio de b2 para guardar la foto del perfil
    // Editar perfil para todos
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> updateProfile(UserDto dto) {
        UUID uuid = UUID.fromString(jwtUtil.extractUserId(dto.getUserId()));
        Optional<User> optionalUser = userRepository.findById(uuid);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("No se pudo encontrar el perfil a editar", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        if (dto.getPassword().isEmpty()) {
            return new ResponseEntity<>(new Message("La contraseña no puede estar vacía", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        User user = optionalUser.get();

        String newEmail = dto.getEmail().trim(); // Correo nuevo
        String currentEmail = user.getEmail().trim(); // Correo del usuario actualmente

        // Si el usuario quiere cambiar su correo
        if (!newEmail.equalsIgnoreCase(currentEmail)) {
            // Mandar el nuevo correo e id del usuario encontrado anteriormente
            Optional<String> duplicateEmail = userRepository.findDuplicateEmail(newEmail, user.getUserId());

            // Sí se encontró el correo
            if (duplicateEmail.isPresent()) {
                return new ResponseEntity<>(new Message("El correo electrónico proporcionado ya está en uso por otro usuario", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        if (dto.getPassword().length() != 60) {
            if (dto.getPassword().length() < 8 || dto.getPassword().length() > 20) {
                return new ResponseEntity<>(new Message("La contraseña debe tener entre 8 y 20 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            String passwordToMatch = "(?=.*[A-Z])(?=.*\\d).{8,20}";
            if (!dto.getPassword().matches(passwordToMatch)) {
                return new ResponseEntity<>(new Message("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula y un número", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user.setName(dto.getName());
        user.setEmail(newEmail);
        user.setProfilePhotoPath(dto.getProfilePhotoPath());
        user.setDescription(dto.getDescription());

        user = userRepository.saveAndFlush(user);
        if (user == null) {
            return new ResponseEntity<>(new Message("Error al editar la información del perfil", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("Información del perfil del usuario editada exitósamente");
        return new ResponseEntity<>(new Message(user, "Perfil editado exitósamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // -------------------------------------------- todo la contraseña se pasara como parametro en el json al mostrar el usuario y al llegar a este método debe llegar encriptada asi como viene en el objeto al mostrar el usaurio
    // Actualizar usuarios por el admin
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> updateForAdmin(UserDto dto) {
        UUID uuid = UUID.fromString(dto.getUserId());
        Optional<User> optionalUser = userRepository.findById(uuid);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("Usuario no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        if (dto.getPassword().isEmpty()) {
            return new ResponseEntity<>(new Message("La contraseña no puede estar vacía", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        String newEmail = dto.getEmail().trim(); // Correo nuevo
        String currentEmail = user.getEmail().trim(); // Correo del usuario actualmente

        // Si el usuario quiere cambiar su correo
        if (!newEmail.equalsIgnoreCase(currentEmail)) {
            // Mandar el nuevo correo e id del usuario encontrado anteriormente
            Optional<String> duplicateEmail = userRepository.findDuplicateEmail(newEmail, user.getUserId());
            // Sí se encontró el correo
            if (duplicateEmail.isPresent()) {
                return new ResponseEntity<>(new Message("El correo electrónico proporcionado ya está en uso por otro usuario", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        if (dto.getPassword().length() != 60) {
            if (dto.getPassword().length() < 8 || dto.getPassword().length() > 20) {
                return new ResponseEntity<>(new Message("La contraseña debe tener entre 8 y 20 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            String passwordToMatch = "(?=.*[A-Z])(?=.*\\d).{8,20}";
            if (!dto.getPassword().matches(passwordToMatch)) {
                return new ResponseEntity<>(new Message("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula y un número", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user.setName(dto.getName());
        user.setEmail(newEmail);

        user = userRepository.saveAndFlush(user);
        if (user == null) {
            return new ResponseEntity<>(new Message("Error al editar la información del usuario", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La actualización del usuario por el admin ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(user, "Información del usuario editada exitósamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Cambiar el estado de instructores por el admin
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatusInstructorForAdmin(UserDto dto) {

        UUID uuid = UUID.fromString(dto.getUserId());

        Optional<User> optionalUser = userRepository.findById(uuid);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la cuenta", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        // Obtener los cursos no finalizados del usuario
        List<Course> courses = courseRepository.findAllNotFinalizedCourses(user.getUserId(), CourseStatus.FINALIZED);

        // Verificar si el usuario tiene cursos no finalizados
        if (!courses.isEmpty()) {
            boolean allCoursesFinalized = courses.stream().allMatch(course -> course.getCourseStatus() == CourseStatus.FINALIZED);
            if (!allCoursesFinalized) {
                logger.info("El docente tiene cursos que no están finalizados.");
                return new ResponseEntity<>(new Message("La cuenta tiene cursos no finalizados", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        user.setStatus(dto.getStatus());
        userRepository.saveAndFlush(user);
        logger.info("El estado del docente ha sido actualizado correctamente");

        return new ResponseEntity<>(new Message(user, "Se cambió el estado de la cuenta correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Cambiar el estado del estudiante por el admin
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatusStudentForAdmin(UserDto dto) {

        UUID uuid = UUID.fromString(dto.getUserId());

        Optional<User> optionalUser = userRepository.findById(uuid);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la cuenta", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        // Obtener los cursos no finalizados del usuario
        List<Registration> registrations = registrationRepository.findAllNotFinalizedCourses(user.getUserId(), RegistrationStatus.FINALIZED);

        // Verificar si el usuario tiene cursos no finalizados
        if (!registrations.isEmpty()) {
            boolean allCoursesFinalized = registrations.stream().allMatch(registration -> registration.getRegistrationStatus() == RegistrationStatus.FINALIZED);
            if (!allCoursesFinalized) {
                logger.info("El estudiante tiene cursos que no están finalizados.");
                return new ResponseEntity<>(new Message("La cuenta tiene cursos no finalizados", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        user.setStatus(dto.getStatus());
        userRepository.saveAndFlush(user);
        logger.info("El estado del estudiante ha sido actualizado correctamente");

        return new ResponseEntity<>(new Message(user, "Se cambió el estado de la cuenta correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Cambiar el estado de instructores por el mismo
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatusInstructor(UserDto dto) {
        UUID uuid = UUID.fromString(jwtUtil.extractUserId(dto.getUserId()));
        Optional<User> optionalUser = userRepository.findById(uuid);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la cuenta", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        if (user.getRole() != UserRole.INSTRUCTOR) {
            logger.info("El usuario no es un docente.");
            return new ResponseEntity<>(new Message("La cuenta no es de tipo docente", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Obtener los cursos no finalizados del usuario
        List<Course> courses = courseRepository.findAllNotFinalizedCourses(user.getUserId(), CourseStatus.FINALIZED);

        // Verificar si el usuario tiene cursos no finalizados
        if (!courses.isEmpty()) {
            boolean allCoursesFinalized = courses.stream().allMatch(course -> course.getCourseStatus() == CourseStatus.FINALIZED);
            if (!allCoursesFinalized) {
                logger.info("El docente tiene cursos que no están finalizados.");
                return new ResponseEntity<>(new Message("La cuenta tiene cursos no finalizados", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        user.setStatus(dto.getStatus());
        userRepository.saveAndFlush(user);
        logger.info("El estado del docente ha sido actualizado correctamente");

        return new ResponseEntity<>(new Message(user, "Se cambió el estado de la cuenta correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Cambiar el estado del estudiante por el estudiante
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatusStudent(UserDto dto) {
        UUID uuid = UUID.fromString(jwtUtil.extractUserId(dto.getUserId()));
        Optional<User> optionalUser = userRepository.findById(uuid);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la cuenta", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        if (user.getRole() != UserRole.STUDENT) {
            logger.info("El usuario no es un estudiante.");
            return new ResponseEntity<>(new Message("La cuenta no es de tipo estudiante", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Obtener los cursos no finalizados del usuario
        List<Registration> registrations = registrationRepository.findAllNotFinalizedCourses(user.getUserId(), RegistrationStatus.FINALIZED);

        // Verificar si el usuario tiene cursos no finalizados
        if (!registrations.isEmpty()) {
            boolean allCoursesFinalized = registrations.stream().allMatch(registration -> registration.getRegistrationStatus() == RegistrationStatus.FINALIZED);
            if (!allCoursesFinalized) {
                logger.info("La cuenta tiene cursos no finalizados");
                return new ResponseEntity<>(new Message("La cuenta tiene cursos no finalizados", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        user.setStatus(dto.getStatus());
        userRepository.saveAndFlush(user);
        logger.info("Se cambió el estado de la cuenta correctamente");

        return new ResponseEntity<>(new Message(user, "Se cambió el estado de la cuenta correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}