package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookView {
    private String title;
    private Integer count;
    private Integer price;
}
