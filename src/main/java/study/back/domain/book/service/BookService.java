package study.back.domain.book.service;

import study.back.domain.book.query.TodayBookQueryDto;
import study.back.global.dto.response.GetBookListResponseDto;
import study.back.domain.book.entity.BookEntity;
import study.back.utils.item.*;


public interface BookService {
    /**
     * @implSpec 검색어와 조건에 맞는 책 리스트를 페이지네이션 정보와 함께 반환
     */
    GetBookListResponseDto getBookList(String query,
                                                               String sort,
                                                               Integer page,
                                                               Integer size,
                                                               String target);

    BookDetail getBookDetail (String isbn);
    TodayBookQueryDto getRecommendBook();
    BookEntity getBookIfExistOrElseNull(String isbn);
}
