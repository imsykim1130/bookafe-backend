package study.back.domain.book.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.back.domain.book.dto.response.GetBookListResponseDto;
import study.back.domain.book.service.BookService;
import study.back.utils.item.BookDetail;
import study.back.utils.item.TodayBookView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookController {

    private final BookService bookService;

    /**
     * 책 검색 리스트 가져오기
     * @param query 검색어
     * @param sort accuracy, latest
     * @param page 현재 페이지
     * @param size 페이지 당 검색 결과수, 기본 10개
     * @param target title, author, publisher
     * @return {@link GetBookListResponseDto}
     */
    @GetMapping("/books/search")
    public ResponseEntity<GetBookListResponseDto> getBookSearchList(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "sort", defaultValue = "accuracy", required = false) String sort,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "target", defaultValue = "title", required = false) String target
                                     ) {
        GetBookListResponseDto responseBody = bookService.getBookList(query, sort, page, size, target);
        return ResponseEntity.ok(responseBody);
    }

    // 책 상세정보
    @GetMapping("/book/detail/{isbn}")
    public ResponseEntity<BookDetail> getBookDetail(@PathVariable(name = "isbn") String isbn) {
       BookDetail data = bookService.getBookDetail(isbn);
       return ResponseEntity.ok(data);
    }

    // 오늘의 책 가져오기
    @GetMapping("/book/today")
    public ResponseEntity<TodayBookView> getRecommendBook() {
        TodayBookView responseDto = bookService.getRecommendBook();
        return ResponseEntity.ok(responseDto);
    }
}
