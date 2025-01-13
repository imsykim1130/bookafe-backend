package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.entity.UserEntity;
import study.back.service.BookCartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class BookCartController {
    private final BookCartService bookCartService;

    @PutMapping("/{isbn}")
    public ResponseEntity add(@PathVariable(name = "isbn") String isbn,
                              @AuthenticationPrincipal UserEntity user) {
        bookCartService.putBookToCart(user, isbn);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity delete(@PathVariable(name = "isbn") String isbn) {
        bookCartService.deleteBookFromCart(isbn);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Boolean> isCart(@PathVariable(name = "isbn") String isbn) {
        boolean result = bookCartService.isCart(isbn);
        return ResponseEntity.ok(result);
    }
}
