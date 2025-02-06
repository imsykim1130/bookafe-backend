package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookCartInfoView {
    Integer count;
    Integer discountedPrice;
    String isbn;
}
