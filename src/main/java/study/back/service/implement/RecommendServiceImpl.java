package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import study.back.entity.BookEntity;
import study.back.entity.RecommendBookEntity;
import study.back.exception.BadRequest.AlreadyRecommendedBookException;
import study.back.exception.BadRequest.NoMoreRecommendBookException;
import study.back.exception.NotFound.NotFoundBookException;
import study.back.repository.origin.BookRepository;
import study.back.repository.origin.RecommendBookRepository;
import study.back.repository.resultSet.RecommendBookView;
import study.back.service.BookService;
import study.back.service.RecommendBookService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendBookService {
    private final RecommendBookRepository recommendBookRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Value("${recommend-book-max-count}")
    private int recommendBookMaxCount;

    // 책 추천하기
    @Override
    public Boolean registerRecommendBook(String isbn) {
        // 책 여부 검증
        BookEntity book = bookService.getBookIfExistOrElseNull(isbn);
        if (book == null) {
            throw new NotFoundBookException();
        }

        // 추천 여부 검증
        if(recommendBookRepository.existsByBook(book)) {
           throw new AlreadyRecommendedBookException();
        }

        // 추천 책 개수 제한 검증
        long count = recommendBookRepository.count();

        if(count >= recommendBookMaxCount) {
            throw new NoMoreRecommendBookException();
        }

        RecommendBookEntity recommendBookEntity = RecommendBookEntity.builder()
                 .book(book)
                 .build();

        recommendBookRepository.save(recommendBookEntity);

        return true;
    }

    // 책 추천 취소하기
    @Override
    public Boolean deleteRecommendBook(String isbn) {
        BookEntity book = bookRepository.findById(isbn).orElseThrow(NotFoundBookException::new);
        recommendBookRepository.deleteByBook(book);
        return true;
    }

    // 추천 책 가져오기
    @Override
    public List<RecommendBookView> getAllRecommendBook() {
        return recommendBookRepository.getAllRecommendBook();
    }

    // 추천 여부 가져오기
    @Override
    public Boolean confirmRecommendBook(String isbn) {
        BookEntity book = bookRepository.findById(isbn).orElseThrow(NotFoundBookException::new);
        return recommendBookRepository.existsByBook(book);
    }
}
