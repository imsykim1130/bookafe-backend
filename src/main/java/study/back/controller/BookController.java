package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.dto.item.CartBookView;
import study.back.dto.request.ChangeCartBookCountRequestDto;
import study.back.dto.response.GetBookListByIsbnListResponseDto;
import study.back.dto.response.GetFavoriteUserIdListResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.UserEntity;
import study.back.repository.resultSet.FavoriteBookView;
import study.back.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookController {

    private final BookService bookService;

    // 책 검색 리스트 가져오기
    // query : 검색어
    // sort : 정확도순, 발간일순
    // page : 현재 페이지
    // size : 한 페이지 당 검색 결과 개수. 기본 10개
    // target : 책 제목, 저자, 춢판사
    @GetMapping("/books/search")
    public ResponseEntity<?> getBookSearchList(@RequestParam(name = "query") String query,
                                     @RequestParam(name = "sort") String sort,
                                     @RequestParam(name = "page") String page,
                                     @RequestParam(name = "size", required = false) String size,
                                     @RequestParam(name = "target") String target
                                     ) {
        return bookService.getBookList(query, sort, page, size, target);
    }

    @GetMapping("/books")
    public ResponseEntity<? super GetBookListByIsbnListResponseDto> getBookList(@RequestParam("isbnList") List<String> isbnList) {
        return bookService.getBookListByIsbnList(isbnList);
    }

    // 책 상세정보
    @GetMapping("/book/detail")
    public ResponseEntity<?> getBookDetail(@RequestParam(name = "isbn") String isbn) {
        return bookService.getBookDetail(isbn);
    }

    // 책 좋아요
    @PutMapping("/book/favorite/{isbn}")
    public ResponseEntity<ResponseDto> putBookFavorite(@PathVariable(name = "isbn") String isbn,
                                                    @AuthenticationPrincipal UserEntity user) {

        return bookService.putFavoriteToBook(isbn, user);
    }

    // 장바구니 담기
    @PutMapping("/book/cart/{isbn}")
    public ResponseEntity<ResponseDto> putBookToCart(@PathVariable(name = "isbn") String isbn,
                                                     @AuthenticationPrincipal UserEntity user) {
        return bookService.putBookToCart(isbn, user);
    }

    // 좋아요 유저 리스트 가져오기
    @GetMapping("/book/{isbn}/favorite/users")
    public ResponseEntity<? super GetFavoriteUserIdListResponseDto> getFavoriteUsers(@PathVariable(name="isbn") String isbn) {
        return bookService.getFavoriteUserList(isbn);
    }

    // 장바구니 유저 리스트 가져오기
    @GetMapping("/book/{isbn}/cart/users")
    public ResponseEntity<?> getCartUsers(@PathVariable(name = "isbn") String isbn) {
        return bookService.getCartUserList(isbn);
    }

    // 좋아요 책 리스트 가져오기
    @GetMapping("/books/favorite")
    public ResponseEntity<?> getFavorite(@AuthenticationPrincipal UserEntity user) {
        return bookService.getFavoriteBookList(user);
    }

    // 좋아요 책 리스트 가져오기 v2
    @GetMapping("/book/favorite-list")
    public ResponseEntity<List<FavoriteBookView>> getFavoriteList(@AuthenticationPrincipal UserEntity user) {
        List<FavoriteBookView> favoriteBookViewList = bookService.getFavoriteBookViewList(user);
        return ResponseEntity.ok(favoriteBookViewList);
    }

    @DeleteMapping("/book/favorite/{isbn}")
    public ResponseEntity<Boolean> deleteBookFavorite(@PathVariable(name = "isbn") String isbn, @AuthenticationPrincipal UserEntity user) {
        bookService.deleteFavoriteBook(user, isbn);
        return ResponseEntity.ok().build();
    }

    // 장바구니 책 리스트 가져오기
    @GetMapping("/books/cart")
    public List<CartBookView> getCart(@AuthenticationPrincipal UserEntity user) {
        return bookService.getCartBookList(user);
    }

    // 장바구니 책 수량 변경
    @PatchMapping("/book/cart/count")
    public ResponseEntity<Integer> setCount(@AuthenticationPrincipal UserEntity user,
                                      @RequestBody ChangeCartBookCountRequestDto requestDto) {
        int changedCount = bookService.changeCartBookCount(requestDto, user);
        return ResponseEntity.ok(changedCount);
    }

    // 장바구니 책 삭제
    @DeleteMapping("/book/cart/{isbn}")
    public ResponseEntity<ResponseDto> deleteCartBook(@PathVariable(name = "isbn") String isbn, @AuthenticationPrincipal UserEntity user) {
        return bookService.deleteCartBook(isbn, user);
    }

    // 장바구니 책 리스트 삭제
    @DeleteMapping("/books/cart")
    public ResponseEntity<?> deleteCartBookList(@RequestBody List<Long> cartBookIdList) {

        boolean isSuccess = bookService.deleteCartBookList(cartBookIdList);

        if(isSuccess) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.internalServerError().body(false);
        }
    }
}
