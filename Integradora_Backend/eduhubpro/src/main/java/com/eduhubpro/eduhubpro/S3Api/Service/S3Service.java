package com.eduhubpro.eduhubpro.S3Api.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.eduhubpro.eduhubpro.S3Api.Config.S3Config;
import com.eduhubpro.eduhubpro.Util.Enum.TypesResponse;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    private final S3Config s3Config;
    private final AmazonS3 amazonS3;

    @Autowired
    public S3Service(S3Config s3Config, AmazonS3 amazonS3) {
        this.s3Config = s3Config;
        this.amazonS3 = amazonS3;
    }

    public ResponseEntity<Message> uploadFile(String folder, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new Message("El archivo está vacío", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        String originalFileName = file.getOriginalFilename().replace(" ", "_");

        File mainFile = new File(originalFileName);

        try (FileOutputStream stream = new FileOutputStream(mainFile)) {
            stream.write(file.getBytes());

            String uuidPlusName = UUID.randomUUID() + "_" + mainFile.getName();
            String newFileName = folder + "/" + uuidPlusName;

            PutObjectRequest request = new PutObjectRequest(s3Config.getBucketName(), newFileName, mainFile);
            amazonS3.putObject(request);

            return new ResponseEntity<>(new Message(uuidPlusName, "Archivo cargado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            Files.delete(mainFile.toPath().getFileName());
            return new ResponseEntity<>(new Message("No fue posible cargar el archivo", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        } finally {
            Files.delete(mainFile.toPath().getFileName());
        }
    }

    // Url para el acceso a archivos con el bucket público
    public ResponseEntity<Message> getFileLocation(String folder, String fileName) {
        String url = amazonS3.getUrl(s3Config.getBucketName(), folder + "/" + fileName).toString();
        return new ResponseEntity<>(new Message(url, "Url del archivo", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // Url pre-firmada para el acceso a archivos con el bucket privado
    public ResponseEntity<Message> getPresignedUrl(String folder, String fileName) {
        logger.info("a {}", fileName);
        try {
            Date expiration = new java.util.Date(System.currentTimeMillis() + 3600000); // 1 hora

            // Generar la URL firmada
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new
                    GeneratePresignedUrlRequest(s3Config.getBucketName(), folder + "/" + fileName)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration);

            // Obtener la URL firmada
            String url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
            return new ResponseEntity<>(new Message(url, "Url del archivo", TypesResponse.SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Error al obtener la url del archivo", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public InputStream downloadFile(String fileName) {
        S3Object object = amazonS3.getObject(s3Config.getBucketName(), fileName);
        return object.getObjectContent();
    }

    public ResponseEntity<Message> getObjectsFromS3() {
        ListObjectsV2Result result = amazonS3.listObjectsV2(s3Config.getBucketName());
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        List<String> list = objects.stream().map(item -> {
            return item.getKey();
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new Message(list, "Listado de archivos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    public boolean deleteFile(String fileName) { // Para eliminación interna
        try {
            // Eliminar el objeto (archivo) del bucket de Backblaze B2
            amazonS3.deleteObject(new DeleteObjectRequest(s3Config.getBucketName(), fileName));
            return true;
        } catch (Exception e) {
            // Manejar posibles excepciones y errores
            return false;
        }
    }

    /*public ResponseEntity<Message> deleteFile(String folder, String fileName) {
        try {
            // Eliminar el archivo utilizando el nombre completo del archivo (carpeta/archivo)
            String objectKey = folder + "/" + fileName;

            // Eliminar el objeto (archivo) del bucket de Backblaze B2 (usando la API S3)
            amazonS3.deleteObject(new DeleteObjectRequest(s3Config.getBucketName(), objectKey));

            // Responder con éxito si la eliminación se completó
            return new ResponseEntity<>(new Message("Archivo eliminado correctamente", "Archivo eliminado", TypesResponse.SUCCESS), HttpStatus.OK);

        } catch (Exception e) {
            // Manejar posibles excepciones y errores
            return new ResponseEntity<>(new Message("No fue posible eliminar el archivo", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
}