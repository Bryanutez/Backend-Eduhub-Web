package com.eduhubpro.eduhubpro.Entity.Category.Service;

import com.eduhubpro.eduhubpro.Entity.Category.Model.Category;
import com.eduhubpro.eduhubpro.Entity.Category.Model.CategoryDto;
import com.eduhubpro.eduhubpro.Entity.Category.Model.CategoryRepository;
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
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // --------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Category> categories = categoryRepository.findAll();
        logger.info("La búsqueda de categorías ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(categories, "Listado de categorías", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(CategoryDto dto) {
        UUID uuid = UUID.fromString(dto.getCategoryId());
        Optional<Category> optionalCategory = categoryRepository.findById(uuid);
        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la categoría", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Category category = optionalCategory.get();
        logger.info("La búsqueda de la categoría ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(category, "Datos de la categoría encontrados exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllActives() {
        List<Category> categories = categoryRepository.findAllByStatusActive(Status.ACTIVE);
        logger.info("Búsqueda de categorías activas realizada correctamente");
        return new ResponseEntity<>(new Message(categories, "Listado de categorías activas", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(CategoryDto dto) {
        Category category = new Category(dto.getName());

        category = categoryRepository.saveAndFlush(category);

        if (category == null) {
            return new ResponseEntity<>(new Message("No se pudo registrar la categoría", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("El registro ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(category, "Categoría registrada correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(CategoryDto dto) {

        UUID uuid = UUID.fromString(dto.getCategoryId());
        Optional<Category> optionalCategory = categoryRepository.findById(uuid);

        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<>(new Message("Categoría no encontrada", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Category category = optionalCategory.get();

        category.setName(dto.getName());
        category = categoryRepository.saveAndFlush(category);

        if (category == null) {
            return new ResponseEntity<>(new Message("Error al editar la categoría", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La actualización de la categoría ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(category, "Categoría editada exitósamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // Cambiar el estado del estudiante por el estudiante
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(CategoryDto dto) {
        UUID uuid = UUID.fromString(dto.getCategoryId());
        Optional<Category> optionalCategory = categoryRepository.findById(uuid);

        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la categoría", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Category category = optionalCategory.get();

        category.setStatus(dto.getStatus());
        category = categoryRepository.saveAndFlush(category);

        if (category == null) {
            return new ResponseEntity<>(new Message("Error al cambiar el estado de la categoría.", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("El estado de la categoría ha sido actualizado correctamente");
        return new ResponseEntity<>(new Message(category, "Se cambió el estado de la categoría correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}