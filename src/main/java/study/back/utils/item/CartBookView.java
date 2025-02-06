package study.back.utils.item;

public interface CartBookView {
    Long getId();
    String getIsbn();
    String getTitle();
    String getAuthor();
    String getBookImg();
    Integer getCount();
    Integer getPrice();
    Integer getDiscountPercent();
}
