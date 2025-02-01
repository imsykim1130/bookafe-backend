package study.back.dto.response;

import lombok.Builder;
import study.back.dto.item.RecommendBookView;

public class GetRecommendBookResponseDto extends ResponseDto {
    private RecommendBookView todayBook;

    @Builder
    public GetRecommendBookResponseDto(String code, String message, RecommendBookView todayBook) {
        super(code, message);
        this.todayBook = todayBook;
    }
}