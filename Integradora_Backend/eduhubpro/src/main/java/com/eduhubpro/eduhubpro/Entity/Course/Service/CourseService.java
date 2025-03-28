package com.eduhubpro.eduhubpro.Entity.Course.Service;

import com.eduhubpro.eduhubpro.Entity.Category.Model.Category;
import com.eduhubpro.eduhubpro.Entity.Category.Model.CategoryRepository;
import com.eduhubpro.eduhubpro.Entity.Course.Model.Course;
import com.eduhubpro.eduhubpro.Entity.Course.Model.CourseDto;
import com.eduhubpro.eduhubpro.Entity.Course.Model.CourseRepository;
import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserRepository;
import com.eduhubpro.eduhubpro.Security.Jwt.JwtUtil;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.CourseStatus;
import com.eduhubpro.eduhubpro.Util.Enum.TypesResponse;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import com.eduhubpro.eduhubpro.Util.Services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Value("${spring.mailersend.courseNotApproved}")
    private String courseNotApproved;

    @Value("7dnvo4d8239l5r86")
    private String courseApproved;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository, JwtUtil jwtUtil, CategoryRepository categoryRepository, EmailService emailService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.categoryRepository = categoryRepository;
        this.emailService = emailService;
    }

    // --------------------------------------------
    // 3.3.2.1.1 Observar cursos pendientes
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllToApprove() {
        List<Course> courses = courseRepository.findAllByStatus(CourseStatus.TO_APPROVE);
        logger.info("Búsqueda de cursos  realizada correctamente");
        return new ResponseEntity<>(new Message(courses, "Listado de cursos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Para mostrar en la página del docente de cursos
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllByInstructor(CourseDto dto) {
        UUID instructorId = UUID.fromString(jwtUtil.extractUserId(dto.getInstructorId()));
        List<Course> courses = courseRepository.findAllByInstructor(instructorId);
        logger.info("Búsqueda de cursos realizada correctamente");
        return new ResponseEntity<>(new Message(courses, "Listado de cursos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Para mostrar en la página principal de cursos
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllPublished() {
        List<Course> courses = courseRepository.findAllByStatus(CourseStatus.PUBLISHED);
        logger.info("Búsqueda de cursos publicados realizada correctamente");
        return new ResponseEntity<>(new Message(courses, "Listado de cursos publicados", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // todo Filtrar por fecha e instructor, nombre, categoría
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findByDate(CourseDto dto) {
        List<Course> courses = courseRepository.findByDate(dto.getStartDate());
        logger.info("Búsqueda de cursos por fecha realizada correctamente");
        return new ResponseEntity<>(new Message(courses, "Listado de cursos por fecha", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findByInstructorName(CourseDto dto) {
        List<Course> courses = courseRepository.findByInstructorName(
                dto.getInstructorId().toLowerCase().trim(),
                CourseStatus.PUBLISHED
        );
        logger.info("Búsqueda de cursos por nombre realizada correctamente");
        return new ResponseEntity<>(new Message(courses, "Listado de cursos por nombre", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(CourseDto dto) {
        UUID instructorId = UUID.fromString(jwtUtil.extractUserId(dto.getInstructorId())); // Viene del jwt
        Optional<User> optionalInstructor = userRepository.findById(instructorId);
        if (optionalInstructor.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró el perfil del docente para relacionar al curso", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        // Obtener los Ids de las categorías desde el DTO
        List<UUID> categoryIds = dto.getCategoriesId().stream()
                .map(UUID::fromString) // Convertir cada ID a UUID
                .collect(Collectors.toList());

        // Consultar todas las categorías por su ID de una sola vez
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            return new ResponseEntity<>(new Message("Algunas categorías no fueron encontradas", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        // La imagen del banner ya debe de llegar en el json
        Course course = new Course(
                dto.getTitle(),
                dto.getDescription(),
                dto.getBannerPath(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getPrice(),
                optionalInstructor.get(),
                categories
        );
        course = courseRepository.saveAndFlush(course);

        if (course == null) {
            return new ResponseEntity<>(new Message("No se pudo registrar el curso", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("El registro ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(course, "El curso ha sido registrado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(CourseDto dto) {
        UUID uuid = UUID.fromString(dto.getCourseId());
        Optional<Course> optionalCourse = courseRepository.findById(uuid);

        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(new Message("Curso no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        // Obtener los Ids de las categorías desde el DTO
        List<UUID> categoryIds = dto.getCategoriesId().stream()
                .map(UUID::fromString) // Convertir cada ID a UUID
                .collect(Collectors.toList());

        // Consultar todas las categorías por su ID de una sola vez
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            return new ResponseEntity<>(new Message("Algunas categorías no fueron encontradas", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Course course = optionalCourse.get();

        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setBannerPath(dto.getBannerPath());
        course.setStartDate(dto.getStartDate());
        course.setEndDate(dto.getEndDate());
        course.setPrice(dto.getPrice());
        course.setCategories(categories);

        course = courseRepository.saveAndFlush(course);

        if (course == null) {
            return new ResponseEntity<>(new Message("Error al editar el curso", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La actualización del curso ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(course, "Curso editado exitósamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // todo al cambiar el estado del curso se cambiará también en inscripción, , y se mandarán constancias
    // todo crear un job que cambie el estado de los cursos a publicados cuando su fecha de inicio de acerque, notificar un dia antes de que el curso empezará

    // --------------------------------------------
    // 3.3.2.1.2 Aceptar/Rechazar
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(CourseDto dto) {
        UUID uuid = UUID.fromString(dto.getCourseId());
        Optional<Course> optionalCourse = courseRepository.findById(uuid);

        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró el curso", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Course course = optionalCourse.get();

        // enviar por correo al instructor que no se aprobó su curso si es el caso
        switch (CourseStatus.valueOf(dto.getCourseStatus())) {
            case CourseStatus.PUBLISHED:
                if (!emailService.sendEmail(
                        "Alan",
                        "alanyr107@gmail.com",
                        "Java",
                        "Hola", courseApproved)
                ) {
                    return new ResponseEntity<>(new Message("Error al enviar la notificación al usuario, inténtalo de nuevo..", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                course.setCourseStatus(CourseStatus.valueOf(dto.getCourseStatus()));
                break;
            case CourseStatus.NOT_APPROVED:
                if (!emailService.sendEmail(
                        "Alan",
                        "alanyr107@gmail.com",
                        "Java",
                        "Hola", courseNotApproved)
                ) {
                    return new ResponseEntity<>(new Message("Error al enviar la notificación al usuario, inténtalo de nuevo..", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                course.setCourseStatus(CourseStatus.valueOf(dto.getCourseStatus()));
                break;
            default:
                return new ResponseEntity<>(new Message("Error al cambiar el estado del curso.", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        course = courseRepository.saveAndFlush(course);

        if (course == null) {
            return new ResponseEntity<>(new Message("Error al cambiar el estado del curso.", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("El estado del curso ha sido actualizado correctamente");
        return new ResponseEntity<>(new Message(course, "Se cambió el estado del curso correctamente.", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}