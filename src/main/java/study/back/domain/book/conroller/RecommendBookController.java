package study.back.domain.book.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.back.utils.item.RecommendBookView;
import study.back.service.RecommendBookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommend-book")
@RequiredArgsConstructor
public class RecommendBookController {
    private final RecommendBookService recommendBookService;

    // 책 추천
    @PostMapping("/{isbn}")
    public ResponseEntity<Boolean> registerRecommendBook(@PathVariable(name = "isbn") String isbn) {
        Boolean result = recommendBookService.registerRecommendBook(isbn);
        return ResponseEntity.ok(result);
    }

    // 추천 여부
    @GetMapping("/is-recommended")
    public ResponseEntity<Boolean> confirmRecommendBook(@RequestParam("isbn") String isbn) {
        Boolean result = recommendBookService.confirmRecommendBook(isbn);
        return ResponseEntity.ok(result);
    }

    // 추천 취소
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Boolean> deleteRecommendBook(@PathVariable("isbn") String isbn) {
        Boolean result = recommendBookService.deleteRecommendBook(isbn);
        return ResponseEntity.ok(result);
    }

    // 추천 책 전체 가져오기
    @GetMapping("/all")
    public ResponseEntity<List<RecommendBookView>> getAllRecommendBook() {
        List<RecommendBookView> result = recommendBookService.getAllRecommendBook();
        return ResponseEntity.ok(result);
    }

}
