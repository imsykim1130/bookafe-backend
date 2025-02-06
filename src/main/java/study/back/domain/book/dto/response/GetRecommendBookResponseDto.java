package study.back.domain.book.dto.response;

import lombok.Getter;
import study.back.utils.ResponseDto;
import study.back.utils.item.TodayBookView;

@Getter
public class GetRecommendBookResponseDto extends ResponseDto {
    private TodayBookView todayBook;

    public GetRecommendBookResponseDto(String code, String message, TodayBookView todayBook) {
        super(code, message);
        this.todayBook = todayBook;
    }
}