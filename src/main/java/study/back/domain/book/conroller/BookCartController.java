package study.back.domain.book.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import study.back.utils.ResponseDto;
import study.back.utils.item.CartBookView;
import study.back.domain.book.dto.request.ChangeCountRequestDto;
import study.back.domain.book.dto.request.DeleteCartBookListRequestDto;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.book.service.BookCartService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
class BookCartController {
    private final BookCartService bookCartService;

    // 장바구니 담기
    @PutMapping("/{isbn}")
    public ResponseEntity<ResponseDto> add(@PathVariable(name = "isbn") String isbn,
                                @AuthenticationPrincipal UserEntity user) {
        bookCartService.putBookToCart(user, isbn);
        ResponseDto responseDto = new ResponseDto("SU", "장바구니 담기 성공");
        return ResponseEntity.ok(responseDto);
    }

    // 장바구니 빼기
    @DeleteMapping("/{isbn}")
    public ResponseEntity<ResponseDto> delete(@PathVariable(name = "isbn") String isbn) {
        bookCartService.deleteBookFromCart(isbn);
        ResponseDto responseDto = new ResponseDto("SU", "장바구니 빼기 성공");
        return ResponseEntity.ok(responseDto);
    }

    // 장바구니 여부
    @GetMapping("/{isbn}")
    public ResponseEntity<Boolean> isCart(@PathVariable(name = "isbn") String isbn, @AuthenticationPrincipal UserEntity user) {
        boolean result = bookCartService.isCart(isbn, user);
        return ResponseEntity.ok(result);
    }

    // 장바구니 담긴 책 리스트
    @GetMapping("/list")
    public ResponseEntity<List<CartBookView>> getBookCartList(@AuthenticationPrincipal UserEntity user) {
        List<CartBookView> bookCartList = bookCartService.getBookCartList(user);
        return ResponseEntity.ok(bookCartList);
    }

    // 장바구니 책 리스트 빼기
    @DeleteMapping("/list")
    public ResponseEntity<ResponseDto> deleteBookCartList(@AuthenticationPrincipal UserEntity user,
                                                @RequestBody DeleteCartBookListRequestDto requestBody) {
        bookCartService.deleteList(requestBody.getCartBookIdList());
        ResponseDto responseDto = new ResponseDto("SU", "장바구니 책 리스트 빼기 성공");
        return ResponseEntity.ok(responseDto);
    }

    // 장바구니 수량 변경
    @PatchMapping("/count")
    public ResponseEntity<ResponseDto> changeCount(@AuthenticationPrincipal UserEntity user,
                                        @RequestBody ChangeCountRequestDto requestBody) {
        System.out.println(requestBody.getCount());
        bookCartService.changeCartBookCount(user, requestBody.getIsbn(), requestBody.getCount());
        ResponseDto responseDto = new ResponseDto("SU", "장바구니 수량 변경 성공");
        return ResponseEntity.ok(responseDto);
    }
}
