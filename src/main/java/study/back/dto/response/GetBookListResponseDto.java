package study.back.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.utils.item.BookSearchItem;
import study.back.utils.item.Meta;
import study.back.utils.ResponseDto;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetBookListResponseDto extends ResponseDto {
    private Boolean isEnd;
    private Integer pageableCount;
    private Integer totalCount;
    private List<BookSearchItem> bookList;

    public GetBookListResponseDto(String code, String message, Meta meta, List<BookSearchItem> bookList) {
        super(code, message);
        this.isEnd = meta.getIs_end();
        this.pageableCount = meta.getPageable_count();
        this.totalCount = meta.getTotal_count();
        this.bookList = bookList;
    }

    public GetBookListResponseDto(Meta meta, List<BookSearchItem> bookList) {
        this("SU", "검색결과 가져오기 성공", meta, bookList);
    }

}
