package study.back.domain.book.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.utils.item.BookPrev;
import study.back.utils.ResponseDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFavoriteBookListResponseDto extends ResponseDto {
    List<BookPrev> bookList = new ArrayList<>();

    public GetFavoriteBookListResponseDto(String code, String message, List<BookPrev> bookList) {
        super(code, message);
        this.bookList = bookList;
    }

    public static ResponseEntity<GetFavoriteBookListResponseDto> success(List<BookPrev> bookPrevList) {
        GetFavoriteBookListResponseDto responseBody = new GetFavoriteBookListResponseDto(
                "SU",
                "책 좋아요 가져오기 성공",
                bookPrevList
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
