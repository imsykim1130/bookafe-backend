package study.back.domain.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCountRequestDto {
    private String isbn;
    private Integer count;
}