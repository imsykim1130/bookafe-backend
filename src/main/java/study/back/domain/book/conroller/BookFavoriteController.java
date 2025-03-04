package study.back.domain.book.conroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import study.back.domain.book.dto.request.DeleteFavoriteBookListRequestDto;
import study.back.domain.book.dto.response.GetBookFavoriteInfoResponseDto;
import study.back.domain.book.dto.response.GetFavoriteBookListResponseDto;
import study.back.domain.book.service.BookFavoriteService;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.ResponseDto;
import study.back.utils.item.Top10View;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class BookFavoriteController {
    private final BookFavoriteService bookFavoriteService;

    // 책 좋아요 정보
    // 좋아요 여부, 좋아요 개수
    @GetMapping("/{isbn}/permit")
    public ResponseEntity<GetBookFavoriteInfoResponseDto> getBookFavoriteInfo(@AuthenticationPrincipal UserEntity user,
                                                                                @PathVariable(name = "isbn") String isbn) {
        GetBookFavoriteInfoResponseDto responseDto = bookFavoriteService.getBookFavoriteInfo(user, isbn);
        return ResponseEntity.ok(responseDto);
    }

    // 좋아요
    @PutMapping("/{isbn}")
    public ResponseEntity<ResponseDto> add(@AuthenticationPrincipal UserEntity user,
                                @PathVariable(name = "isbn") String isbn) {
        bookFavoriteService.putBookToFavorite(user, isbn);
        ResponseDto responseDto = new ResponseDto("SU", "좋아요 성공");
        return ResponseEntity.ok(responseDto);
    }

    // 좋아요 취소
    @DeleteMapping("/{isbn}")
    public ResponseEntity<ResponseDto> delete(@AuthenticationPrincipal UserEntity user,
                                    @PathVariable(name = "isbn") String isbn) {
        bookFavoriteService.deleteBookFromFavorite(user, isbn);
        return ResponseEntity.ok().build();
    }

    // 좋아요 책 일괄 취소
    @DeleteMapping("/list")
    public ResponseEntity<ResponseDto> deleteFavoriteBookList(@AuthenticationPrincipal UserEntity user,
                                                                @RequestBody @Valid DeleteFavoriteBookListRequestDto requestDto) {
        bookFavoriteService.deleteBookListFromFavorite(user, requestDto.getIsbnList());
        ResponseDto responseDto = new ResponseDto("SU", "좋아요 책 일괄취소 성공");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
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
