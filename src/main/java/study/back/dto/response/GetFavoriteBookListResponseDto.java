package study.back.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.utils.ResponseDto;
import study.back.utils.item.FavoriteBookView;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFavoriteBookListResponseDto extends ResponseDto {
    List<FavoriteBookView> favoriteBookList = new ArrayList<>();
    Boolean isEnd;
    Integer totalPages;

    public GetFavoriteBookListResponseDto(String code, String message, List<FavoriteBookView> favoriteBookList, Boolean isEnd, Integer totalPages) {
        super(code, message);
        this.favoriteBookList = favoriteBookList;
        this.isEnd = isEnd;
        this.totalPages = totalPages;
    }
}
