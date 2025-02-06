package study.back.domain.book.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeCartBookCountRequestDto {
    private int count;
    private String isbn;
}
