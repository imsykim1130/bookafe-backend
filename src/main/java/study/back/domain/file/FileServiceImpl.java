package study.back.domain.file;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import study.back.exception.InternalServerError.UploadFailException;

@Service
@Transactional
public class FileServiceImpl implements FileService {
    @Resource
    private Cloudinary cloudinary;
        // private final String uploadDir = "/Users/gimsoyeong/Desktop/all/toy-project/bookafe/bookafe-back/src/main/resources/static/image/";

    // // 이미지 업로드(로컬 저장)
    // // 입력: 이미지 파일
    // // 출력: 이미지 저장 경로
    // @Override
    // public String upload(MultipartFile file) {
    //     String uniqueFileName;

    //     try {
    //         String originalFilename = file.getOriginalFilename();
    //         // 확장자 추출
    //         String extension = "";
    //         if (originalFilename != null && originalFilename.contains(".")) {
    //             extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    //         }
    //         // uuid 로 고유 파일 이름 생성
    //          uniqueFileName = UUID.randomUUID() + extension;

    //         // 로컬 저장 경로 생성
    //         Path path = Paths.get(uploadDir + uniqueFileName);

    //         // 파일 저장
    //         File directory = new File(uploadDir);
    //         if (!directory.exists()) {
    //             directory.mkdirs(); // 디렉토리가 없으면 생성
    //         }

    //         file.transferTo(path.toFile());
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         throw new UploadFailException();
    //     }

    //     return uniqueFileName;
    // }

    // 이미지를 url 로 변환
    @Override
    public String upload(MultipartFile file, String folderName, String fileName) throws IOException {
        String publicID = (String) cloudinary
                .uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("public_id", fileName , "folder", folderName, "invalidate", true))
                .get("public_id");
        return cloudinary.url().generate(publicID);
    }

    // 이미지 삭제
    @Override
    public void delete(String imageUrl, String folderName) throws IOException {
        String publicID = extractPublicId(imageUrl, folderName);
        cloudinary.uploader().destroy(publicID, ObjectUtils.emptyMap());
    }

    private String extractPublicId(String imageUrl, String folderName) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        return folderName + "/" + fileName; // 폴더 경로 포함
    }
    
}
