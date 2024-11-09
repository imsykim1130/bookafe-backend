package study.back.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
public class ResponseDto {
    String code;
    String message;

    // 성공
    public static ResponseEntity<ResponseDto> success(String message) {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "SU";
        responseBody.message = message;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    // 중복된 유저
    public static ResponseEntity<ResponseDto> existedUser() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "EU";
        responseBody.message = "이미 존재하는 회원입니다";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    // 중복된 닉네임
    public static ResponseEntity<ResponseDto> existedNickname() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "EN";
        responseBody.message = "이미 존재하는 닉네임입니다";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    // 존재하지 않는 유저
    public static ResponseEntity<ResponseDto> notFoundUser() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "NU";
        responseBody.message = "유저 정보가 존재하지 않습니다";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    // 로그인 정보 불일치
    public static ResponseEntity<ResponseDto> authFail() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "AF";
        responseBody.message = "로그인 정보 불일치";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    // 책 정보 찾을 수 없음
    public static ResponseEntity<ResponseDto> notFoundBook() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "NB";
        responseBody.message = "찾으시는 책 정보가 없습니다";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    // 댓글 찾을 수 없음
    public static ResponseEntity<ResponseDto> notFoundComment() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "NC";
        responseBody.message = "해당 댓글이 없습니다";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    // 댓글 내용 없음
    public static ResponseEntity<ResponseDto> notFoundCommentContent() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "NCC";
        responseBody.message = "댓글 내용이 없습니다";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    // 서버 에러
    public static ResponseEntity<ResponseDto> internalServerError() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "SE";
        responseBody.message = "서버 에러";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    // 데이터베이스 에러
    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "DBE";
        responseBody.message = "데이터베이스 에러";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

}
