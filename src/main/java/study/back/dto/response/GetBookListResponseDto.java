package study.back.dto.response;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.dto.item.BookPrev;
import study.back.dto.item.Meta;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBookListResponseDto extends ResponseDto {
    private Meta meta;
    private List<BookPrev> bookList;

    public static ResponseEntity<GetBookListResponseDto> success(Meta meta, List<BookPrev> BookPrevList) {

        GetBookListResponseDto responseBody = new GetBookListResponseDto();
        responseBody.code = "SU";
        responseBody.message = "책 검색 성공";
        responseBody.meta = meta;
        responseBody.bookList = BookPrevList;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
