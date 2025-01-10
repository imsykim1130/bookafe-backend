package study.back.dto.item;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderBookItem {
    private String isbn;
    private Integer count;
}
