package study.back.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import study.back.dto.item.*;
import study.back.dto.response.*;
import study.back.entity.BookEntity;
import study.back.exception.KakaoAuthorizationException;
import study.back.repository.BookRepositoryInterface;
import study.back.repository.impl.BookRepositoryImpl;
import study.back.repository.origin.BookRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BookService {

    private BookRepositoryInterface repository;

    @Autowired
    public BookService(BookRepository bookRepository, EntityManager em) {
        this.repository = new BookRepositoryImpl(bookRepository, em);
    }

    @Value("${kakao-authorization}")
    private String kakaoAuthorization;

    @Value("${recommend-book-max-count}")
    private int recommendBookMaxCount;

    // 카카오 api 에서 책 정보 가져오기
    private OriginBookItem getBookDataFromKakaoApi(String query, String sort, String page, String size, String target) throws KakaoAuthorizationException {
        OriginBookItem result;
        RestClient restClient = RestClient.builder()
                .baseUrl("https://dapi.kakao.com/v3/search/book")
                .defaultHeader("Authorization", kakaoAuthorization)
                .build();

        result = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", query)
                        .queryParam("sort", sort)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("target", target)
                        .build())
                .retrieve()
                .body(OriginBookItem.class);
        return result;
    }

    // 책 검색 리스트 가져오기
    public ResponseEntity<? super GetBookListResponseDto> getBookList(String query,
                                                                      String sort,
                                                                      String page,
                                                                      String size,
                                                                      String target) {
        // 카카오 api 를 통해 받은 데이터 형태
        OriginBookItem result;

        try {
            // kakao api 에서 책 정보 받기
            result = getBookDataFromKakaoApi(query, sort, page, size, target);

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

    // 책 정보 가져오기
    // db, 카카오 api 에 모두 찾는 책 정보가 없으면 null 반환
    // db 에 데이터가 없으면 카카오 api 에서 정보를 찾은 뒤 db 에 넣어준다
    private BookEntity getBookIfExistOrElseNull(String isbn) {
        BookEntity book = null;
        Optional<BookEntity> bookOptional = repository.findBookByIsbn(isbn);
        if (bookOptional.isPresent()) {
            book = bookOptional.get();
        }

        if(book == null) {
            OriginBookItem bookDataFromKakaoApi = getBookDataFromKakaoApi(isbn.split(" ")[0], "accuracy", null, null, "isbn");
            List<BookItem> bookItemList = bookDataFromKakaoApi.getDocuments();
            if(!bookItemList.isEmpty()) {
                BookItem bookItem = bookItemList.getFirst();
                book = BookEntity.toEntity(bookItem);
                repository.saveBook(book);
            }
        }
        return book;
    }

    // 책 상세 정보 가져오기
    public ResponseEntity<? super GetBookDetailResponseDto> getBookDetail (String isbn) {
        BookDetail bookDetail = null;
        
        try {
            // 책 유무 확인
            BookEntity book = getBookIfExistOrElseNull(isbn);

            if(book != null) {
                bookDetail = BookDetail.createBookDetail(book);
            }
            // 두 경우 다 책을 찾을 수 없을 때
            if(bookDetail == null) {
                return GetBookDetailResponseDto.notFoundBook();
            }
        } catch (Exception e) {
            // 서버 에러
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        // 책 정보 찾기 성공
        return GetBookDetailResponseDto.success(bookDetail);
    }

    // 추천 책 가져오기
    public RecommendBookView getRecommendBook() {
        // todo
        RecommendBookView result = repository.findRecommendBook(recommendBookMaxCount);
        return result;
    }
}
