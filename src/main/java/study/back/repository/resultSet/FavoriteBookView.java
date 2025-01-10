package study.back.repository.resultSet;

public interface FavoriteBookView {
   String getIsbn();
   String getBookImg();
   String getTitle();
   String getAuthor();
   Integer getPrice();
   Integer getDiscountPercent();
   Integer getIsCart();
}
