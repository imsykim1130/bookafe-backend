package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.back.dto.item.RecommendBookView;
import study.back.service.BookService;

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

    // 책 상세정보
    @GetMapping("/book/detail/{isbn}")
    public ResponseEntity<?> getBookDetail(@PathVariable(name = "isbn") String isbn) {
        return bookService.getBookDetail(isbn);
    }

    // 추천책 가져오기
    @GetMapping("/book/recommend")
    public ResponseEntity<RecommendBookView> getRecommendBook() {
        RecommendBookView result = bookService.getRecommendBook();
        return ResponseEntity.ok(result);
    }

}
