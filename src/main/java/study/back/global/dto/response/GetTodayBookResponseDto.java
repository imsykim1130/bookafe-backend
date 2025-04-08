package study.back.global.dto.response;

import lombok.Getter;
import study.back.global.dto.ResponseDto;
import study.back.domain.book.query.TodayBookQueryDto;

@Getter
public class GetTodayBookResponseDto extends ResponseDto {
    private TodayBookQueryDto todayBook;

    public GetTodayBookResponseDto(String code, String message, TodayBookQueryDto todayBook) {
        super(code, message);
        this.todayBook = todayBook;
    }

    public GetTodayBookResponseDto(TodayBookQueryDto todayBook) {
        this("SU", "오늘의 책 가져오기 성공", todayBook);
    }
}