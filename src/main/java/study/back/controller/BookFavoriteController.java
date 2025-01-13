package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.entity.UserEntity;
import study.back.service.BookFavoriteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class BookFavoriteController {
    private final BookFavoriteService bookFavoriteService;

    @GetMapping("/{isbn}")
    public ResponseEntity<Boolean> isFavorite(@AuthenticationPrincipal UserEntity user,
                                              @PathVariable(name = "isbn") String isbn) {
        boolean result = bookFavoriteService.isFavorite(user, isbn);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity add(@AuthenticationPrincipal UserEntity user,
                              @PathVariable(name = "isbn") String isbn) {
        bookFavoriteService.putBookToFavorite(user, isbn);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity delete(@AuthenticationPrincipal UserEntity user,
                                 @PathVariable(name = "isbn") String isbn) {
        bookFavoriteService.deleteBookFromFavorite(user, isbn);
        return ResponseEntity.ok().build();
    }

}
