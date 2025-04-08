package study.back.domain.book.service;

import study.back.domain.book.query.RecommendBookQueryDto;

import java.util.List;

public interface RecommendBookService {
    Boolean registerRecommendBook(String isbn);
    Boolean deleteRecommendBook(String isbn);
    List<RecommendBookQueryDto> getAllRecommendBook();
    Boolean confirmRecommendBook(String isbn);
}
