package study.back.service;

import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file);
    ResponseEntity<UrlResource> getProfileImage(String fileName);
}
