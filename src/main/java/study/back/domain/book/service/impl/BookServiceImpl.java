package study.back.domain.book.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.back.domain.book.dto.response.GetBookDetailResponseDto;
import study.back.domain.book.dto.response.GetBookListResponseDto;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.repository.BookRepository;
import study.back.domain.book.service.BookService;
import study.back.domain.book.service.KakaoService;
import study.back.exception.NotFound.NotFoundBookException;
import study.back.utils.ResponseDto;
import study.back.utils.item.*;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final KakaoService kakaoService;

    @Value("${recommend-book-max-count}")
    private int recommendBookMaxCount;

    // 책 정보 가져오기
    // db, 카카오 api 에 모두 찾는 책 정보가 없으면 null 반환
    // db 에 데이터가 없으면 카카오 api 에서 정보를 찾은 뒤 db 에 넣어준다
    @Override
    public BookEntity getBookIfExistOrElseNull(String isbn) {
        BookEntity book = null;
        Optional<BookEntity> bookOptional = repository.findBookByIsbn(isbn);

        if (bookOptional.isPresent()) {
            book = bookOptional.get();
        }

        if(book == null) {
            OriginBookItem bookDataFromKakaoApi = kakaoService.getBookDataFromKakaoApi(
                    isbn.split(" ")[0], "accuracy", null, null, "isbn");
            List<BookItem> bookItemList = bookDataFromKakaoApi.getDocuments();
            if(!bookItemList.isEmpty()) {
                BookItem bookItem = bookItemList.getFirst();
                book = BookEntity.toEntity(bookItem);
                repository.saveBook(book);
            }
        }

        return book;
    }

    // 책 검색 리스트 가져오기
    @Override
    public ResponseEntity<? super GetBookListResponseDto> getBookList(String query,
                                                                      String sort,
                                                                      Integer page,
                                                                      Integer size,
                                                                      String target) {
        // 카카오 api 를 통해 받은 데이터 형태
        OriginBookItem result;

        try {
            // kakao api 에서 책 정보 받기
            result = kakaoService.getBookDataFromKakaoApi(query, sort, page.toString(), size.toString(), target);

        }
        catch (Exception e) {
            // 서버 에러
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        // 가져온 데이터 db 에 저장하기
        List<BookEntity> bookEntityList = result.getDocuments()
                .stream()
                .map(bookItem -> BookEntity.toEntity(bookItem))
                .toList();

        // 검색 성공
        List<BookPrev> bookPrevList = bookEntityList
                .stream()
                .map(BookPrev::createBookPrev)
                .toList();
        return GetBookListResponseDto.success(result.getMeta(), bookPrevList);
    }



    // 책 상세 정보 가져오기
    @Override
    public ResponseEntity<GetBookDetailResponseDto> getBookDetail (String isbn) {
        BookDetail bookDetail = null;

        // 책 유무 확인
        BookEntity book = getBookIfExistOrElseNull(isbn);

        if(book != null) {
            bookDetail = BookDetail.createBookDetail(book);
        }

        // db, 카카오 api 두 경우 다 책을 찾을 수 없을 때
        if(bookDetail == null) {
            throw new NotFoundBookException();
        }

        // 책 정보 찾기 성공
        return GetBookDetailResponseDto.success(bookDetail);
    }

    // 추천 책 가져오기
    @Override
    public TodayBookView getRecommendBook() {
        // todo
        return repository.findRecommendBook(recommendBookMaxCount);
    }
}
