package study.back.global.exception.InternalServerError;

import study.back.global.exception.errors.InternalServerErrorException;

public class UploadFailException extends InternalServerErrorException {
    public UploadFailException() {
        super("UF", "파일 업로드 실패");
    }
}
