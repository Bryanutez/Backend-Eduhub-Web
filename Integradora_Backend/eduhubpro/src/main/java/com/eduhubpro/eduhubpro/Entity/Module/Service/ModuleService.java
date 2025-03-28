package com.eduhubpro.eduhubpro.Entity.Module.Service;

import com.eduhubpro.eduhubpro.Entity.Course.Model.Course;
import com.eduhubpro.eduhubpro.Entity.Course.Model.CourseRepository;
import com.eduhubpro.eduhubpro.Entity.Module.Model.Module;
import com.eduhubpro.eduhubpro.Entity.Module.Model.ModuleDto;
import com.eduhubpro.eduhubpro.Entity.Module.Model.ModuleRepository;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.ModuleStatus;
import com.eduhubpro.eduhubpro.Util.Enum.TypesResponse;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleService {

    private static final Logger logger = LoggerFactory.getLogger(ModuleService.class);

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ModuleService(ModuleRepository moduleRepository, CourseRepository courseRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
    }

    // --------------------------------------------
    // Mostrar todos los módulos activos relacionados con un curso (perfil docente)
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllByCourseId(ModuleDto dto) {
        UUID courseId = UUID.fromString(dto.getCourseId());
        List<Module> modules = moduleRepository.findAllByCourseId(courseId, Arrays.asList(ModuleStatus.UNLOCKED, ModuleStatus.LOCKED));
        if (modules.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontraron los módulos del curso", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        logger.info("Búsqueda de secciones realizada correctamente");
        return new ResponseEntity<>(new Message(modules, "Módulos del curso encontrados exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(ModuleDto dto) {
        UUID moduleId = UUID.fromString(dto.getModuleId());
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if (optionalModule.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró el módulo", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Module module = optionalModule.get();
        logger.info("La búsqueda del módulo ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(module, "Datos del módulo encontrados exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(ModuleDto dto) {
        UUID courseId = UUID.fromString(dto.getCourseId());
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró el curso relacionado al módulo", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        // El nombre del archivo del video debe de venir en el json después de guardarse en S3
        Module module = new Module(dto.getName(), dto.getDate(), optionalCourse.get());
        module = moduleRepository.saveAndFlush(module);

        if (module == null) {
            return new ResponseEntity<>(new Message("No se pudo registrar el módulo del curso", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("El registro ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(module, "El módulo del curso ha sido registrado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(ModuleDto dto) {
        UUID moduleId = UUID.fromString(dto.getModuleId());
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);

        if (optionalModule.isEmpty()) {
            return new ResponseEntity<>(new Message("Módulo no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Module module = optionalModule.get();

        module.setName(dto.getName());
        module.setDate(dto.getDate());

        module = moduleRepository.saveAndFlush(module);

        if (module == null) {
            return new ResponseEntity<>(new Message("Error al editar el módulo", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Actualización del módulo realizada correctamente");
        return new ResponseEntity<>(new Message(module, "Módulo editado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // Agregar dos métodos de cambiar estado, uno para la eliminación de módulos para el docente (antes de que comience el curso) y otro para desbloquear módulos por el estudiante
    // --------------------------------------------
    // Solo se podrá cambiar el estado del módulo (eliminar) cuando el estado del curso no esté aún publicado, (manejar desde el front)
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(ModuleDto dto) {
        UUID moduleId = UUID.fromString(dto.getModuleId());
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);

        if (optionalModule.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró el módulo", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Module module = optionalModule.get();

        module.setStatus(dto.getStatus());
        moduleRepository.saveAndFlush(module);

        logger.info("El estado del módulo ha sido actualizado correctamente");
        return new ResponseEntity<>(new Message(module, "Se cambió el estado del módulo correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
