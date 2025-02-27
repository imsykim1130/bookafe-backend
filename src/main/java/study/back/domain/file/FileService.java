package study.back.domain.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String upload(MultipartFile file, String folderName, String fileName) throws IOException;
    void delete(String imageUrl, String folderName) throws IOException;
}
