package study.back.dto.response;

import lombok.Getter;
import study.back.utils.ResponseDto;
import study.back.utils.item.TodayBookView;

@Getter
public class GetTodayBookResponseDto extends ResponseDto {
    private TodayBookView todayBook;

    public GetTodayBookResponseDto(String code, String message, TodayBookView todayBook) {
        super(code, message);
        this.todayBook = todayBook;
    }

    public GetTodayBookResponseDto(TodayBookView todayBook) {
        this("SU", "오늘의 책 가져오기 성공", todayBook);
    }
}