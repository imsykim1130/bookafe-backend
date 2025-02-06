package study.back.domain.book.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class DeleteCartBookListRequestDto {
    private List<Long> cartBookIdList;
}
