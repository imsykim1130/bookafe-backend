package study.back.domain.book.service;

import study.back.utils.item.RecommendBookView;

import java.util.List;

public interface RecommendBookService {
    Boolean registerRecommendBook(String isbn);
    Boolean deleteRecommendBook(String isbn);
    List<RecommendBookView> getAllRecommendBook();
    Boolean confirmRecommendBook(String isbn);
}
