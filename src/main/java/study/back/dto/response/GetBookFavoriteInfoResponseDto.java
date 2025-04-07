package study.back.dto.response;

import lombok.Getter;
import study.back.utils.ResponseDto;

@Getter
public class GetBookFavoriteInfoResponseDto extends ResponseDto {
    private Boolean isFavorite;
    private Integer favoriteCount;

    public GetBookFavoriteInfoResponseDto(String code, String message, Boolean isFavorite, Integer favoriteCount) {
        super(code, message);
        this.isFavorite = isFavorite;
        this.favoriteCount = favoriteCount;
    }

    public GetBookFavoriteInfoResponseDto(Boolean isFavorite, Integer favoriteCount) {
        this("SU", "책 좋아요 정보 가져오기 성공", isFavorite, favoriteCount);
    }
}
