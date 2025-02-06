package study.back.domain.book.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.utils.item.BookCart;
import study.back.utils.ResponseDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetCartBookListResponseDto extends ResponseDto {
    private List<BookCart> bookList = new ArrayList<>();

    public static ResponseEntity<GetCartBookListResponseDto> success(List<BookCart> bookCartList) {
        GetCartBookListResponseDto responseBody = new GetCartBookListResponseDto();
        responseBody.code = "SU";
        responseBody.message = "장바구니 책 리스트 가져오기 성공";
        responseBody.bookList = bookCartList;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
