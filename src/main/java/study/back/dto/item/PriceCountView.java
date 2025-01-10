package study.back.dto.item;

import study.back.entity.BookEntity;

public interface PriceCountView {
    Integer getPrice();
    Integer getCount();
    Integer getDiscountPercent();
    BookEntity getBook();
}
