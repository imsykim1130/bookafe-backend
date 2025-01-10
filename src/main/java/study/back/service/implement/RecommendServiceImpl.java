package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.entity.BookEntity;
import study.back.entity.RecommendBookEntity;
import study.back.exception.AlreadyRecommendedBookException;
import study.back.exception.NoMoreRecommendBookException;
import study.back.exception.NotExistBookException;
import study.back.repository.BookRepository;
import study.back.repository.RecommendBookRepository;
import study.back.repository.resultSet.RecommendBookView;
import study.back.service.RecommendBookService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendBookService {
    private final RecommendBookRepository recommendBookRepository;
    private final BookRepository bookRepository;

    @Override
    public Boolean registerRecommendBook(String isbn) {
        // 책 여부 검증
        BookEntity book = bookRepository.findById(isbn).orElseThrow(() -> new NotExistBookException("책이 존재하지 않습니다"));

        // 추천 여부 검증
        if(recommendBookRepository.existsByBook(book)) {
           throw new AlreadyRecommendedBookException("이미 추천된 책입니다.");
        }

        // 추천 책 개수 제한 검증
        int maxNumber = 3;
        long count = recommendBookRepository.count();

        if(count >= maxNumber) {
            throw new NoMoreRecommendBookException("이미 책 추천이 모두 완료되었습니다");
        }

        RecommendBookEntity recommendBookEntity = RecommendBookEntity.builder()
                 .book(book)
                 .build();

        recommendBookRepository.save(recommendBookEntity);

        return true;
    }

    @Override
    public Boolean deleteRecommendBook(String isbn) {
        BookEntity book = bookRepository.findById(isbn).orElseThrow(() -> new NotExistBookException("책이 존재하지 않습니다"));
        recommendBookRepository.deleteByBook(book);
        return true;
    }

    @Override
    public List<RecommendBookView> getAllRecommendBook() {
        return recommendBookRepository.getAllRecommendBook();
    }

    @Override
    public Boolean confirmRecommendBook(String isbn) {
        BookEntity book = bookRepository.findById(isbn).orElseThrow(() -> new NotExistBookException("책이 존재하지 않습니다"));
        return recommendBookRepository.existsByBook(book);
    }
}
