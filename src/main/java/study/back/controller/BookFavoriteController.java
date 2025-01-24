package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.user.entity.UserEntity;
import study.back.repository.resultSet.FavoriteBookView;
import study.back.repository.resultSet.Top10View;
import study.back.service.BookFavoriteService;

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
    public ResponseEntity<List<FavoriteBookView>> favoriteList(@AuthenticationPrincipal UserEntity user) {
        List<FavoriteBookView> result = bookFavoriteService.getFavoriteBookList(user);
        return ResponseEntity.ok(result);
    }

    // top10 가져오기
    @GetMapping("/top10")
    public ResponseEntity<List<Top10View>> favoriteTop10() {
        List<Top10View> result = bookFavoriteService.getTop10BookList();
        return ResponseEntity.ok(result);
    }

}
