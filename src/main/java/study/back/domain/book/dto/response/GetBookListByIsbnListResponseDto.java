package study.back.domain.book.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import study.back.utils.item.BookCart;
import study.back.utils.ResponseDto;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBookListByIsbnListResponseDto extends ResponseDto {
    private List<BookCart> bookList;

    public GetBookListByIsbnListResponseDto(String code, String message, List<BookCart> bookList) {
        super(code, message);
        this.bookList = bookList;
    }

    public static ResponseEntity<GetBookListByIsbnListResponseDto> success(List<BookCart> bookList) {
        GetBookListByIsbnListResponseDto responseDto = new GetBookListByIsbnListResponseDto(
                "SU",
                "isbn 리스트로 책 정보 가져오기 성공",
                bookList
        );

        return ResponseEntity.ok(responseDto);
    }

}
