package study.back.utils.item;

import study.back.domain.book.entity.BookEntity;

public interface PriceCountView {
    Integer getPrice();
    Integer getCount();
    Integer getDiscountPercent();
    BookEntity getBook();
}
