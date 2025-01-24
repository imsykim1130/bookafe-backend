package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import study.back.user.entity.UserEntity;
import study.back.service.FileService;
import study.back.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    // 파일 업로드
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public String uploadFile(@AuthenticationPrincipal UserEntity user, @RequestPart("file") MultipartFile file) {
        String fileName = fileService.upload(file);
//        userService.changeProfileImage(user, fileName);
        return fileName;
    }
}
