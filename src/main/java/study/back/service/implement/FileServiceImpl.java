package study.back.service.implement;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.back.exception.InternalServerError.UploadFailException;
import study.back.service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
public class FileServiceImpl implements FileService {
    private final String uploadDir = "/Users/gimsoyeong/Desktop/all/toy-project/bookafe/bookafe-back/src/main/resources/static/image/";

    // 이미지 업로드
    // 입력: 이미지 파일
    // 출력: 이미지 저장 경로
    @Override
    public String upload(MultipartFile file) {
        String uniqueFileName;

        try {
            String originalFilename = file.getOriginalFilename();
            // 확장자 추출
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            // uuid 로 고유 파일 이름 생성
             uniqueFileName = UUID.randomUUID() + extension;

            // 로컬 저장 경로 생성
            Path path = Paths.get(uploadDir + uniqueFileName);

            // 파일 저장
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리가 없으면 생성
            }

            file.transferTo(path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            throw new UploadFailException();
        }

        return uniqueFileName;
    }
}
