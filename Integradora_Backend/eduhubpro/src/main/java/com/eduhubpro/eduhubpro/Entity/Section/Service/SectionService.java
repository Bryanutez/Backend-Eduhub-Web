package com.eduhubpro.eduhubpro.Entity.Section.Service;

import com.eduhubpro.eduhubpro.Entity.Module.Model.Module;
import com.eduhubpro.eduhubpro.Entity.Module.Model.ModuleRepository;
import com.eduhubpro.eduhubpro.Entity.Section.Model.Section;
import com.eduhubpro.eduhubpro.Entity.Section.Model.SectionDto;
import com.eduhubpro.eduhubpro.Entity.Section.Model.SectionRepository;
import com.eduhubpro.eduhubpro.S3Api.Service.S3Service;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SectionService {

    private static final Logger logger = LoggerFactory.getLogger(SectionService.class);

    private final SectionRepository sectionRepository;
    private final ModuleRepository moduleRepository;
    private final S3Service s3Service;

    @Autowired
    public SectionService(SectionRepository sectionRepository, ModuleRepository moduleRepository, S3Service s3Service) {
        this.sectionRepository = sectionRepository;
        this.moduleRepository = moduleRepository;
        this.s3Service = s3Service;
    }

    // --------------------------------------------
    // Mostrar todas las secciones activas relacionadas con un módulo (perfil docente)
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllByModuleId(SectionDto dto) {
        UUID moduleId = UUID.fromString(dto.getModuleId());
        List<Section> sections = sectionRepository.findAllByModuleId(moduleId, Status.ACTIVE);
        if (sections.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontraron las secciones del módulo", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        logger.info("Búsqueda de secciones realizada correctamente");
        return new ResponseEntity<>(new Message(sections, "Secciones del módulo encontradas exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(SectionDto dto) {
        UUID sectionId = UUID.fromString(dto.getSectionId());
        Optional<Section> optionalSection = sectionRepository.findById(sectionId);
        if (optionalSection.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la sección", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Section section = optionalSection.get();
        logger.info("La búsqueda de la sección ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(section, "Datos de la sección encontrados exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(SectionDto dto) {
        UUID moduleId = UUID.fromString(dto.getModuleId());
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if (optionalModule.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró el módulo relacionado a la sección", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        // El nombre del archivo del video debe de venir en el json después de guardarse en S3
        Section section = new Section(dto.getName(), dto.getDescription(), dto.getContentUrl(), optionalModule.get());
        section = sectionRepository.saveAndFlush(section);

        if (section == null) {
            return new ResponseEntity<>(new Message("No se pudo registrar la sección del módulo", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("El registro ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(section, "La sección del módulo ha sido registrada correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(SectionDto dto) {
        UUID sectionId = UUID.fromString(dto.getSectionId());
        Optional<Section> optionalSection = sectionRepository.findById(sectionId);

        if (optionalSection.isEmpty()) {
            return new ResponseEntity<>(new Message("Sección no encontrada", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Section section = optionalSection.get();

        section.setName(dto.getName());
        section.setDescription(dto.getDescription());
        section.setContentUrl(dto.getContentUrl());

        section = sectionRepository.saveAndFlush(section);

        if (section == null) {
            return new ResponseEntity<>(new Message("Error al editar la sección", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Actualización de la sección realizada correctamente");
        return new ResponseEntity<>(new Message(section, "Sección editada correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Solo se podrá cambiar el estado de la sección (eliminar) cuando el estado del curso no esté aún publicado
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(SectionDto dto) {
        UUID sectionId = UUID.fromString(dto.getSectionId());
        Optional<Section> optionalSection = sectionRepository.findById(sectionId);

        if (optionalSection.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la sección", TypesResponse.ERROR), HttpStatus.NOT_FOUND);

        }
        Section section = optionalSection.get();

        // Al eliminar una sección se eliminará también el contenido en S3
        if (dto.getStatus() == Status.INACTIVE) {
            if (!s3Service.deleteFile(section.getContentUrl())) {
                return new ResponseEntity<>(new Message("Hubo un error, vuelve a intentar eliminar la sección", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        section.setStatus(dto.getStatus());
        sectionRepository.saveAndFlush(section);

        logger.info("El estado de la sección ha sido actualizado correctamente");
        return new ResponseEntity<>(new Message(section, "Se cambió el estado de la sección correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
