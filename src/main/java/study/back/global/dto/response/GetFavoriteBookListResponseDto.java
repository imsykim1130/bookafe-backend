package study.back.global.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.global.dto.ResponseDto;
import study.back.domain.book.query.FavoriteBookQueryDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFavoriteBookListResponseDto extends ResponseDto {
    List<FavoriteBookQueryDto> favoriteBookList = new ArrayList<>();
    Boolean isEnd;
    Integer totalPages;

    public GetFavoriteBookListResponseDto(String code, String message, List<FavoriteBookQueryDto> favoriteBookList, Boolean isEnd, Integer totalPages) {
        super(code, message);
        this.favoriteBookList = favoriteBookList;
        this.isEnd = isEnd;
        this.totalPages = totalPages;
    }
}
