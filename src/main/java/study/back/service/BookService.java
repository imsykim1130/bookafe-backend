package study.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import study.back.dto.item.*;
import study.back.dto.request.ChangeCartBookCountRequestDto;
import study.back.dto.response.*;
import study.back.entity.BookCartEntity;
import study.back.entity.BookEntity;
import study.back.entity.BookFavorite;
import study.back.entity.UserEntity;
import study.back.exception.KakaoAuthorizationException;
import study.back.exception.NotExistBookException;
import study.back.repository.origin.BookCartRepository;
import study.back.repository.origin.BookFavoriteRepository;
import study.back.repository.origin.BookRepository;
import study.back.repository.origin.UserRepository;
import study.back.repository.resultSet.FavoriteBookView;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookFavoriteRepository bookFavoriteRepository;
    private final BookCartRepository bookCartRepository;
    private final UserRepository userRepository;

    @Value("${kakao-authorization}")
    private String kakaoAuthorization;

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
        Optional<BookEntity> bookOptional = bookRepository.findById(isbn);
        if (bookOptional.isPresent()) {
            book = bookOptional.get();
        }

        if(book == null) {
            OriginBookItem bookDataFromKakaoApi = getBookDataFromKakaoApi(isbn.split(" ")[0], "accuracy", null, null, "isbn");
            List<BookItem> bookItemList = bookDataFromKakaoApi.getDocuments();
            if(!bookItemList.isEmpty()) {
                BookItem bookItem = bookItemList.getFirst();
                book = BookEntity.toEntity(bookItem);
                bookRepository.save(book);
            }
        }
        return book;
    }

    public ResponseEntity<? super GetBookListByIsbnListResponseDto> getBookListByIsbnList(List<String> isbnList) {
        List<BookCart> bookList;
        try {
            bookList = bookRepository.findAllByIsbnList(isbnList)
                    .stream().map(BookCart::createBookCart).toList();

        } catch (Exception e) {
            return ResponseDto.internalServerError();
        }
        return GetBookListByIsbnListResponseDto.success(bookList);
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

    // 책에 좋아요 누르기
    public ResponseEntity<ResponseDto> putFavoriteToBook(String isbn, UserEntity user) {
        System.out.println("---- 책 좋아요 누르기");
        try {
            // 책 찾기
            Optional<BookEntity> bookOpt = bookRepository.findById(isbn);

            // 책 찾기 실패
            if(bookOpt.isEmpty()) {
                return ResponseDto.notFoundBook();
            }
            BookEntity book = bookOpt.get();

            // 좋아요 누르기 / 취소하기
            // 좋아요 상태 확인하기
            Optional<BookFavorite> bookFavoriteOptional = bookFavoriteRepository.findByUserAndBook(user, book);

            if(bookFavoriteOptional.isEmpty()) {
                // 좋아요 누르기
                BookFavorite bookFavorite = BookFavorite.createBookFavorite(user, book);
                bookFavoriteRepository.save(bookFavorite);
            } else {
                // 좋아요 취소하기
                bookFavoriteRepository.delete(bookFavoriteOptional.get());
            }

        } catch (Exception e) {
            // 서버 에러
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        return ResponseDto.success("좋아요 누르기 성공");
    }

    // 좋아요 누른 책 리스트 가져오기
    public ResponseEntity<? super GetFavoriteBookListResponseDto> getFavoriteBookList(UserEntity user) {
        System.out.println("좋아요 책 리스트 가져오기");
        List<BookPrev> bookPrevList = null;
        try {
            bookPrevList = bookRepository.findFavoriteBookListByUser(user)
                    .stream()
                    .map(bookEntity -> BookPrev.createBookPrev(bookEntity))
                    .toList();
        } catch (Exception e) {
            // 서버 오류
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }

        return GetFavoriteBookListResponseDto.success(bookPrevList);
    }

    // 좋아요 책 리스트 가져오기 v2
    public List<FavoriteBookView> getFavoriteBookViewList(UserEntity user) {
        return bookRepository.findFavoriteBookViewList(user);
    }

    // 책 장바구니 담기
    public ResponseEntity<ResponseDto> putBookToCart(String isbn, UserEntity user) {
        System.out.println("---- 장바구니 담기 / 빼기");
        try {
            // 책 찾기
            BookEntity book = getBookIfExistOrElseNull(isbn);
            // 책 찾기 실패
            if(book == null) {
                return ResponseDto.notFoundBook();
            }

            Optional<BookCartEntity> bookCartOpt = bookCartRepository.findByUserAndBook(user, book);
            if(bookCartOpt.isEmpty()) {
                BookCartEntity bookCart = BookCartEntity.createBookCart(user, isbn);
                bookCartRepository.save(bookCart);
            } else {
                // delete 문을 실행하기 전에 select 문이 자동으로 실행된다
                bookCartRepository.deleteById(Long.valueOf(bookCartOpt.get().getId()));
            }

        } catch (Exception e) {
            // 서버 에러
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        return ResponseDto.success("장바구니 담기 / 빼기 성공");
    }

    // 장바구니 담은 책 가져오기
    public List<CartBookView> getCartBookList(UserEntity user) {
        System.out.println("장바구니 책 가져오기");
        List<CartBookView> cartBookViewList = null;
        try {
            cartBookViewList = bookCartRepository.findCartBookViewByUser(user);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("서버 에러");
        }
        return cartBookViewList;
    }

    public ResponseEntity<? super GetCartUserIdListResponseDto> getCartUserList(String isbn) {
        System.out.println("--- 해당 책을 카트에 담은 유저 가져오기");
        List<String> userIdList = null;
        try {
            // 책 유무 확인
            Optional<BookEntity> bookOpt = bookRepository.findById(isbn);
            if(bookOpt.isEmpty()) {
                return ResponseDto.notFoundBook();
            }
            BookEntity book = bookOpt.get();
            // 해당 책을 카트에 담은 유저 가져오기
            userIdList = userRepository.getCartUserIdListByBook(book);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        return GetCartUserIdListResponseDto.success(userIdList);
    }

    public ResponseEntity<? super GetFavoriteUserIdListResponseDto> getFavoriteUserList(String isbn) {
        System.out.println("--- 해당 책을 좋아요 한 유저 id 리스트 가져오기");
        List<String> userIdList = null;
        try {
            // 책 유무 확인
            Optional<BookEntity> bookOpt = bookRepository.findById(isbn);
            if(bookOpt.isEmpty()) {
                return ResponseDto.notFoundBook();
            }
            BookEntity book = bookOpt.get();
            // 해당 책을 좋아요 한 유저 가져오기
            userIdList = userRepository.getFavoriteUserIdListByBook(book);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        return GetFavoriteUserIdListResponseDto.success(userIdList);
    }

    // 장바구니 책 삭제
    public ResponseEntity<ResponseDto> deleteCartBook(String isbn, UserEntity user) {
        System.out.println("장바구니 책 삭제");
        try {
            bookCartRepository.deleteCartBook(user.getId(), isbn);
        } catch (Exception e) {
            return ResponseDto.internalServerError();
        }
        return ResponseDto.success("장바구니 책 삭제 성공");
    }

    // 장바구니 책 수량 변경
    // in : 장바구니 수량 변경하고자 하는 책 isbn, 유저
    // out : 변경된 수량 반환
    public int changeCartBookCount(ChangeCartBookCountRequestDto requestDto, UserEntity user) {
        bookCartRepository.updateBookCartCount(requestDto.getCount(), user, requestDto.getIsbn());
        return bookCartRepository.getBookCartCount(user, requestDto.getIsbn());
    }

    // 장바구니 책 리스트 삭제
    public boolean deleteCartBookList(List<Long> cartBookIdList) {
        try {
            bookCartRepository.deleteAllByIdList(cartBookIdList);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 좋아요 책 삭제
    public int deleteFavoriteBook(UserEntity user, String isbn) {
        BookEntity book = bookRepository.findById(isbn).orElseThrow(() -> new NotExistBookException("존재하지 않는 책입니다"));
        return bookFavoriteRepository.deleteFavoriteBookByUserAndBook(user, book);
    }

}
