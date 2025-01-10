package study.back.service;

import study.back.repository.resultSet.RecommendBookView;

import java.util.List;

public interface RecommendBookService {
    Boolean registerRecommendBook(String isbn);
    Boolean deleteRecommendBook(String isbn);
    List<RecommendBookView> getAllRecommendBook();
    Boolean confirmRecommendBook(String isbn);
}
