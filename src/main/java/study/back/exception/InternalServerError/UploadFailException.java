package study.back.exception.InternalServerError;

import study.back.exception.errors.InternalServerErrorException;

public class UploadFailException extends InternalServerErrorException {
    public UploadFailException() {
        super("UF", "파일 업로드 실패");
    }
}
