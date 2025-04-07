package study.back.global.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class DeleteFavoriteBookListRequestDto {
    @NotNull(message = "isbn 리스트는 필수 입력사항입니다")
    private List<String> isbnList;

    @AssertTrue(message = "좋아요를 취소할 책이 없습니다")
    public boolean isNotEmpty() {
        return !isbnList.isEmpty();
    }
}
