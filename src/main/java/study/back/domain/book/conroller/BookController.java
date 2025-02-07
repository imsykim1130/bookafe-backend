package study.back.domain.book.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.back.domain.book.dto.response.GetBookDetailResponseDto;
import study.back.domain.book.dto.response.GetBookListResponseDto;
import study.back.domain.book.dto.response.GetRecommendBookResponseDto;
import study.back.domain.book.service.BookService;
import study.back.utils.item.TodayBookView;

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
    public ResponseEntity<GetBookListResponseDto> getBookSearchList(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "sort", defaultValue = "accuracy", required = false) String sort,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "target", defaultValue = "title", required = false) String target
                                     ) {
        return bookService.getBookList(query, sort, page, size, target);
    }

    // 책 상세정보
    @GetMapping("/book/detail/{isbn}")
    public ResponseEntity<GetBookDetailResponseDto> getBookDetail(@PathVariable(name = "isbn") String isbn) {
        return bookService.getBookDetail(isbn);
    }

    // 추천책 가져오기
    @GetMapping("/book/recommend")
    public ResponseEntity<GetRecommendBookResponseDto> getRecommendBook() {
        TodayBookView result = bookService.getRecommendBook();
        GetRecommendBookResponseDto responseDto =
                new GetRecommendBookResponseDto("SU", "추천책 가져오기 성공", result);
        return ResponseEntity.ok(responseDto);
    }

}
