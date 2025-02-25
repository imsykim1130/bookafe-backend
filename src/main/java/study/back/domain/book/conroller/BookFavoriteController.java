package study.back.domain.book.conroller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.domain.book.dto.response.GetFavoriteBookListResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.Top10View;
import study.back.domain.book.service.BookFavoriteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class BookFavoriteController {
    private final BookFavoriteService bookFavoriteService;

    // 좋아요 여부
    @GetMapping("/{isbn}")
    public ResponseEntity<Boolean> isFavorite(@AuthenticationPrincipal UserEntity user,
                                              @PathVariable(name = "isbn") String isbn) {
        boolean result = bookFavoriteService.isFavorite(user, isbn);
        return ResponseEntity.ok(result);
    }

    // 좋아요
    @PutMapping("/{isbn}")
    public ResponseEntity add(@AuthenticationPrincipal UserEntity user,
                              @PathVariable(name = "isbn") String isbn) {
        bookFavoriteService.putBookToFavorite(user, isbn);
        return ResponseEntity.ok().build();
    }

    // 좋아요 취소
    @DeleteMapping("/{isbn}")
    public ResponseEntity delete(@AuthenticationPrincipal UserEntity user,
                                 @PathVariable(name = "isbn") String isbn) {
        bookFavoriteService.deleteBookFromFavorite(user, isbn);
        return ResponseEntity.ok().build();
    }

    // 좋아요 책 리스트 가져오기
    @GetMapping("/list")
    public ResponseEntity<GetFavoriteBookListResponseDto> favoriteList(@AuthenticationPrincipal UserEntity user,
                                                                       @RequestParam(name = "page") @Min(value = 0, message = "페이지는 음수가 될 수 없습니다") Integer page,
                                                                       @RequestParam(name = "size", defaultValue = "10", required = false) @Positive(message = "한 번에 가져올 데이터의 수는 1 이상이어야 합니다") Integer size) {
        GetFavoriteBookListResponseDto result = bookFavoriteService.getFavoriteBookList(user, page, size);
        return ResponseEntity.ok(result);
    }

    // top10 가져오기
    @GetMapping("/top10")
    public ResponseEntity<List<Top10View>> favoriteTop10() {
        List<Top10View> result = bookFavoriteService.getTop10BookList();
        return ResponseEntity.ok(result);
    }
}
