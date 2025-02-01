package study.back.dto.response;

import lombok.Getter;
import study.back.dto.item.RecommendBookView;

@Getter
public class GetRecommendBookResponseDto extends ResponseDto {
    private RecommendBookView todayBook;

    public GetRecommendBookResponseDto(String code, String message, RecommendBookView todayBook) {
        this.code = code;
        this.message = message;
        this.todayBook = todayBook;
    }
}