package study.back.domain.book.service;

import org.springframework.http.ResponseEntity;
import study.back.domain.book.dto.response.GetBookDetailResponseDto;
import study.back.domain.book.dto.response.GetBookListResponseDto;
import study.back.domain.book.entity.BookEntity;
import study.back.utils.item.*;


public interface BookService {
    GetBookListResponseDto getBookList(String query,
                                                               String sort,
                                                               Integer page,
                                                               Integer size,
                                                               String target);

    BookDetail getBookDetail (String isbn);
    TodayBookView getRecommendBook();
    BookEntity getBookIfExistOrElseNull(String isbn);
}
