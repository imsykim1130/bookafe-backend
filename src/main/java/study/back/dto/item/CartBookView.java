package study.back.dto.item;

public interface CartBookView {
    Long getId();
    String getIsbn();
    String getTitle();
    String getAuthor();
    String getImg();
    Integer getCount();
    Integer getPrice();
    Integer getDiscountPercent();
}
