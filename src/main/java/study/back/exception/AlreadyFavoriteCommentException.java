package study.back.exception;

public class AlreadyFavoriteCommentException extends RuntimeException {
    public AlreadyFavoriteCommentException() {
        super("이미 좋아요를 누른 리뷰입니다");
    }
}
