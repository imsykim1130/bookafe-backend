package study.back.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.utils.item.BookDetail;
import study.back.utils.ResponseDto;

@Getter
public class GetBookDetailResponseDto extends ResponseDto {
    private BookDetail book;

    public GetBookDetailResponseDto(String code, String message, BookDetail book) {
        super(code, message);
        this.book = book;
    }

    public static ResponseEntity<GetBookDetailResponseDto> success(BookDetail bookDetail) {
        GetBookDetailResponseDto responseBody = new GetBookDetailResponseDto("SU", "책 상세정보 가져오기 성공", bookDetail);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notFoundBook() {
        ResponseDto responseBody = new ResponseDto("NB", "해당 책 정보를 찾을 수 없습니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
