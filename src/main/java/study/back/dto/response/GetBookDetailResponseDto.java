package study.back.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.dto.item.BookDetail;

@Getter
public class GetBookDetailResponseDto extends ResponseDto{
    private BookDetail book;

    public static ResponseEntity<GetBookDetailResponseDto> success(BookDetail bookDetail) {
        GetBookDetailResponseDto responseBody = new GetBookDetailResponseDto();
        responseBody.code = "SU";
        responseBody.message = "책 정보 가져오기 성공";
        responseBody.book = bookDetail;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notFoundBook() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "NB";
        responseBody.message = "해당 책 정보를 찾을 수 없습니다.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
