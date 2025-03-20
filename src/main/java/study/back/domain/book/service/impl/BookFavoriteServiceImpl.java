package study.back.domain.book.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import study.back.domain.book.dto.response.GetBookFavoriteInfoResponseDto;
import study.back.domain.book.dto.response.GetFavoriteBookListResponseDto;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.exception.Conflict.AlreadyFavoriteBookException;
import study.back.exception.Conflict.AlreadyRecommendedBookException;
import study.back.exception.NotFound.NotFoundBookException;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.book.repository.BookFavoriteRepository;
import study.back.domain.book.repository.impl.BookFavoriteRepoImpl;
import study.back.domain.book.repository.jpa.BookFavoriteJpaRepository;
import study.back.domain.book.repository.jpa.BookJpaRepository;
import study.back.utils.item.FavoriteBookView;
import study.back.utils.item.Top10View;
import study.back.domain.book.service.BookFavoriteService;

import java.util.List;

@Service
@Transactional
public class BookFavoriteServiceImpl implements BookFavoriteService {
    private final BookFavoriteRepository repository;
    
    public BookFavoriteServiceImpl(BookFavoriteJpaRepository bookFavoriteJpaRepository, BookJpaRepository bookJpaRepository, EntityManager em) {
        this.repository = new BookFavoriteRepoImpl(bookFavoriteJpaRepository, bookJpaRepository, em);
    }

    // 책의 좋아요 관련 정보 가져오기
    @Override
    public GetBookFavoriteInfoResponseDto getBookFavoriteInfo(UserEntity user, String isbn) {
        // 책 여부 검증
        Boolean isExistsedBook = repository.existsBook(isbn);
        if(!isExistsedBook) {
            throw new NotFoundBookException();
        }

        // 책에 좋아요를 누른 유저의 id 리스트
        List<Long> userIdList = repository.findFavoriteBookUserIdList(isbn);

        // 유저 id 리스트에 해당 유저가 있는지 여부
        boolean isFavorite;
        // 로그인 되어있지 않은 상황이라 user 가 없으면 무조건 false 반환
        if(user == null) {
            isFavorite = false;
        } else {
         isFavorite = userIdList.contains(user.getId());
        }
        // 해당 책을 좋아요 한 유저의 수
        Integer favoriteCount = userIdList.size();

       return new GetBookFavoriteInfoResponseDto(isFavorite, favoriteCount);
    }

    @Override
    public void putBookToFavorite(UserEntity user, String isbn) {
        // 책 여부 검증
        BookEntity book = repository.findBookByIsbn(isbn).orElseThrow(NotFoundBookException::new);

        // 좋아요 여부 검증
        Boolean isFavorite = repository.existsBookFavoriteByUserAndIsbn(user, isbn);
        if(isFavorite) {
            throw new AlreadyFavoriteBookException();
        }

        // 저장
        BookFavoriteEntity bookFavorite = BookFavoriteEntity.createBookFavorite(user, book);
        repository.saveBookFavorite(bookFavorite);
    }

    // 좋아요 취소
    @Override
    public void deleteBookFromFavorite(UserEntity user, String isbn) {
        // 좋아요 여부 검증
        BookFavoriteEntity bookFavorite = repository.findBookFavoriteByUserAndIsbn(user, isbn).orElseThrow(() -> new RuntimeException("이미 좋아요가 해제된 책입니다"));
        repository.deleteBookFavorite(bookFavorite);
    }

    // 좋아요 책 일괄 취소
    @Override
    public int deleteBookListFromFavorite(UserEntity user, List<String> isbnList) {
        return repository.deleteAllFavoriteBook(user, isbnList);
    }

    // 좋아요 책 가져오기
    @Override
    public GetFavoriteBookListResponseDto getFavoriteBookList(UserEntity user, Integer page, Integer size) {
        // page 는 필수 입력값
        // size 는 기본 10
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<FavoriteBookView> result = repository.findAllFavoriteBookView(user, pageRequest);
        return new GetFavoriteBookListResponseDto("SU", "좋아요 책 리스트 가져오기 성공", result.getContent(), result.isLast(), result.getTotalPages());
    }

    // top10 가져오기
    @Override
    public List<Top10View> getTop10BookList() {
        return repository.findAllTop10View();
    }
}
