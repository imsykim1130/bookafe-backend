package study.back.domain.book.conroller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.domain.book.dto.request.DeleteFavoriteBookListRequestDto;
import study.back.domain.book.dto.response.GetBookFavoriteInfoResponseDto;
import study.back.domain.book.dto.response.GetBookListResponseDto;
import study.back.domain.book.dto.response.GetFavoriteBookListResponseDto;
import study.back.domain.book.service.BookFavoriteService;
import study.back.domain.book.service.BookService;
import study.back.domain.book.service.RecommendBookService;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.ResponseDto;
import study.back.utils.item.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookController {

    private final BookService bookService;
    private final BookFavoriteService bookFavoriteService;
    private final RecommendBookService recommendBookService;

    /**
     * 책 검색 리스트 가져오기
     * @param query 검색어
     * @param sort accuracy, latest
     * @param page 현재 페이지
     * @param size 페이지 당 검색 결과수, 기본 10개
     * @param target title, author, publisher
     * @return {@link GetBookListResponseDto}
     */
    @GetMapping("/books")
    public ResponseEntity<GetBookListResponseDto> getBookSearchList(
            @NotEmpty(message = "잘못된 검색어입니다") @RequestParam(name = "query") String query,
            @RequestParam(name = "sort", defaultValue = "accuracy", required = false) String sort,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "target", defaultValue = "title", required = false) String target
                                     ) {
        GetBookListResponseDto responseBody = bookService.getBookList(query, sort, page, size, target);
        return ResponseEntity.ok(responseBody);
    }

    /**
     * 책 상세정보
     * @param isbn
     * @return 책 상세정보 리스트 {@link BookDetail}
     */
    @GetMapping("/book")
    public ResponseEntity<BookDetail> getBookDetail(@NotEmpty(message = "잘못된 isbn 입니다") @RequestParam("isbn") String isbn) {
       BookDetail data = bookService.getBookDetail(isbn);
       return ResponseEntity.ok(data);
    }

    /**
     * 오늘의 책 가져오기 <br/>
     * 관리자가 추천한 책 중 하나를 랜덤하게 가져옴
     * @return 오늘의 책 {@link TodayBookView}
     */
    @GetMapping("/book/today")
    public ResponseEntity<TodayBookView> getRecommendBook() {
        TodayBookView responseDto = bookService.getRecommendBook();
        return ResponseEntity.ok(responseDto);
    }

    /**
     * <pre>책 좋아요 정보</pre>
     * @param user 헤더에서 jwt 에서 얻은 로그인 정보. 로그인 하지 않은 상태이면 null
     * @param isbn 책의 isbn
     * @return {@link GetBookFavoriteInfoResponseDto}
     * @apiNote 로그인 되지 않은 상태에서는 유저의 책에 대한 좋아요 여부 무조건 false 로 반환
     */
    @GetMapping("/book/like-info")
    public ResponseEntity<GetBookFavoriteInfoResponseDto> getBookFavoriteInfo(@AuthenticationPrincipal UserEntity user,
                                                                              @NotEmpty(message = "잘못된 isbn 입니다") @RequestParam(name = "isbn") String isbn) {
        GetBookFavoriteInfoResponseDto responseDto = bookFavoriteService.getBookFavoriteInfo(user, isbn);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 좋아요 책 리스트 가져오기
     * @param user jwt 에서 추출
     * @param page 가져올 페이지
     * @param size 페이지 당 가져올 데이터 개수
     * @return 좋아요 책 리스트 {@link FavoriteBookView}, 마지막 페이지 여부, 총 페이지 수
     */
    @GetMapping("/books/like")
    public ResponseEntity<GetFavoriteBookListResponseDto> favoriteList(@AuthenticationPrincipal UserEntity user,
                                                                       @Min(value = 0, message = "페이지는 음수가 될 수 없습니다") @RequestParam(name = "page") Integer page,
                                                                       @Positive(message = "한 번에 가져올 데이터의 수는 1 이상이어야 합니다") @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {
        GetFavoriteBookListResponseDto result = bookFavoriteService.getFavoriteBookList(user, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * top10 가져오기
     * @return 좋아요 개수가 높은 순대로 10 개의 책 리스트 {@link Top10View}
     */
    @GetMapping("/books/top10")
    public ResponseEntity<List<Top10View>> favoriteTop10() {
        List<Top10View> result = bookFavoriteService.getTop10BookList();
        return ResponseEntity.ok(result);
    }

    /**
     * 좋아요
     * @param user jwt 에서 추출
     * @param isbn 좋아요 누를 책의 isbn
     */
    @PostMapping("/book/like")
    public ResponseEntity<ResponseDto> add(@AuthenticationPrincipal UserEntity user,
                                           @NotEmpty(message = "잘못된 isbn 입니다") @RequestParam(name = "isbn") String isbn) {
        bookFavoriteService.putBookToFavorite(user, isbn);
        ResponseDto responseDto = new ResponseDto("SU", "좋아요 성공");
        return ResponseEntity.ok(responseDto);
    }

    // 좋아요 취소
    @DeleteMapping("/book/like")
    public ResponseEntity<ResponseDto> delete(@AuthenticationPrincipal UserEntity user,
                                              @NotEmpty(message = "잘못된 isbn 입니다") @RequestParam(name = "isbn") String isbn) {
        bookFavoriteService.deleteBookFromFavorite(user, isbn);
        return ResponseEntity.ok().build();
    }

    // 좋아요 책 일괄 취소
    @DeleteMapping("/books/like")
    public ResponseEntity<ResponseDto> deleteFavoriteBookList(@AuthenticationPrincipal UserEntity user,
                                                              @RequestBody @Valid DeleteFavoriteBookListRequestDto requestDto) {
        bookFavoriteService.deleteBookListFromFavorite(user, requestDto.getIsbnList());
        ResponseDto responseDto = new ResponseDto("SU", "좋아요 책 일괄취소 성공");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 책 추천
    @PostMapping("/admin/book/recommend")
    public ResponseEntity<Boolean> registerRecommendBook(@NotEmpty(message = "잘못된 isbn 입니다") @RequestParam("isbn") String isbn) {
        Boolean result = recommendBookService.registerRecommendBook(isbn);
        return ResponseEntity.ok(result);
    }

    // 책 추천 취소
    @DeleteMapping("/admin/book/recommend")
    public ResponseEntity<Boolean> deleteRecommendBook(@NotEmpty(message = "잘못된 isbn 입니다") @RequestParam("isbn") String isbn) {
        Boolean result = recommendBookService.deleteRecommendBook(isbn);
        return ResponseEntity.ok(result);
    }

    // 책 추천 여부
    @GetMapping("/admin/book/is-recommended")
    public ResponseEntity<Boolean> confirmRecommendBook(@NotEmpty(message = "잘못된 isbn 입니다") @RequestParam("isbn") String isbn) {
        Boolean result = recommendBookService.confirmRecommendBook(isbn);
        return ResponseEntity.ok(result);
    }

    // 추천 책 전체 가져오기
    @GetMapping("/admin/books/recommend")
    public ResponseEntity<List<RecommendBookView>> getAllRecommendBook() {
        List<RecommendBookView> result = recommendBookService.getAllRecommendBook();
        return ResponseEntity.ok(result);
    }
}
