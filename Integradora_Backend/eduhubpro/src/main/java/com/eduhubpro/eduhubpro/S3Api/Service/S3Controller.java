package com.eduhubpro.eduhubpro.S3Api.Service;

import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserRepository;
import com.eduhubpro.eduhubpro.S3Api.Dto.S3Dto;
import com.eduhubpro.eduhubpro.Security.Jwt.JwtUtil;
import com.eduhubpro.eduhubpro.Util.Enum.S3Folder;
import com.eduhubpro.eduhubpro.Util.Enum.TypesResponse;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/storage")
public class S3Controller {
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public S3Controller(S3Service s3Service, UserRepository userRepository, JwtUtil jwtUtil) {
        this.s3Service = s3Service;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<Message> uploadPdf(@RequestPart(value = "file") MultipartFile file) throws IOException {
        return s3Service.uploadFile(S3Folder.PDF.toString(), file);
    }

    @PostMapping("/upload-profile-photo")
    public ResponseEntity<Message> uploadProfilePhoto(@RequestPart(value = "file") MultipartFile file) throws IOException {
        return s3Service.uploadFile(S3Folder.PROFILE_PHOTOS.toString(), file);
    }

    @GetMapping("/get-profile-photo")
    public ResponseEntity<Message> getProfileImage(@Validated(S3Dto.GetProfilePhotoUrl.class) @RequestBody S3Dto dto) {
        UUID uuid = UUID.fromString(jwtUtil.extractUserId(dto.getUserId()));
        Optional<User> optionalUser = userRepository.findById(uuid);
        if (optionalUser.get().getProfilePhotoPath()== null) {
            return new ResponseEntity<>(new Message("El usuario no tiene foto de perfil", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        return s3Service.getPresignedUrl(S3Folder.PROFILE_PHOTOS.toString(), optionalUser.get().getProfilePhotoPath());
    }

    @GetMapping("/object-list")
    public ResponseEntity<Message> getObjectList() {
        return s3Service.getObjectsFromS3();
    }

    @GetMapping(value = "/download")
    public ResponseEntity<Resource> download(@RequestBody String fileName) {
        InputStreamResource resource = new InputStreamResource(s3Service.downloadFile(fileName));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(resource);
    }
}