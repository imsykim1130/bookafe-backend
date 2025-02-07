package study.back.domain.book.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.utils.item.BookSearchItem;
import study.back.utils.item.Meta;
import study.back.utils.ResponseDto;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBookListResponseDto extends ResponseDto {
    private Meta meta;
    private List<BookSearchItem> bookList;

    public GetBookListResponseDto(String code, String message, Meta meta, List<BookSearchItem> bookList) {
        super(code, message);
        this.meta = meta;
        this.bookList = bookList;
    }

    public static ResponseEntity<GetBookListResponseDto> success(Meta meta, List<BookSearchItem> bookList) {

        GetBookListResponseDto responseBody = new GetBookListResponseDto(
                "SU",
                "책 검색 성공",
                meta,
                bookList
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
