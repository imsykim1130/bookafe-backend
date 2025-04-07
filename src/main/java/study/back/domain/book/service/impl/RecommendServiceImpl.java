package study.back.domain.book.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.RecommendBookEntity;
import study.back.domain.book.service.BookService;
import study.back.domain.book.service.RecommendBookService;
import study.back.global.exception.Conflict.AlreadyRecommendedBookException;
import study.back.global.exception.BadRequest.NoMoreRecommendBookException;
import study.back.global.exception.NotFound.NotFoundBookException;
import study.back.domain.book.repository.jpa.BookJpaRepository;
import study.back.domain.book.repository.jpa.RecommendBookJpaRepository;
import study.back.utils.item.RecommendBookView;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendBookService {
    private final RecommendBookJpaRepository recommendBookJpaRepository;
    private final BookJpaRepository bookJpaRepository;
    private final BookService bookService;

    // 책 추천하기
    @Override
    public Boolean registerRecommendBook(String isbn) {
        // 책 여부 검증
        BookEntity book = bookService.getBookIfExistOrElseNull(isbn);
        if (book == null) {
            throw new NotFoundBookException();
        }

        // 추천 여부 검증
        if(recommendBookJpaRepository.existsByBook(book)) {
           throw new AlreadyRecommendedBookException();
        }

        // 추천 책 개수 제한 검증
        long count = recommendBookJpaRepository.count();

        int recommendBookMaxCount = 10;
        if(count >= recommendBookMaxCount) {
            throw new NoMoreRecommendBookException();
        }

        RecommendBookEntity recommendBookEntity = RecommendBookEntity.builder()
                 .book(book)
                 .build();

        recommendBookJpaRepository.save(recommendBookEntity);

        return true;
    }

    // 책 추천 취소하기
    @Override
    public Boolean deleteRecommendBook(String isbn) {
        BookEntity book = bookJpaRepository.findById(isbn).orElseThrow(NotFoundBookException::new);
        recommendBookJpaRepository.deleteByBook(book);
        return true;
    }

    // 추천 책 가져오기
    @Override
    public List<RecommendBookView> getAllRecommendBook() {
        return recommendBookJpaRepository.getAllRecommendBook();
    }

    // 추천 여부 가져오기
    @Override
    public Boolean confirmRecommendBook(String isbn) {
        BookEntity book = bookJpaRepository.findById(isbn).orElseThrow(NotFoundBookException::new);
        return recommendBookJpaRepository.existsByBook(book);
    }
}
