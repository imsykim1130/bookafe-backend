package study.back.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.dto.item.BookCart;
import study.back.dto.item.BookPrev;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFavoriteBookListResponseDto extends ResponseDto {
    List<BookPrev> bookList = new ArrayList<>();

    public static ResponseEntity<GetFavoriteBookListResponseDto> success(List<BookPrev> bookPrevList) {
        GetFavoriteBookListResponseDto responseBody = new GetFavoriteBookListResponseDto();
        responseBody.code = "SU";
        responseBody.message = "책 좋아요 리스트 가져오기 성공";
        responseBody.bookList = bookPrevList;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
