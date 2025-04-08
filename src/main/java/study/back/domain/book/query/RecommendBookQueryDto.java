package study.back.domain.book.query;

public interface RecommendBookQueryDto {
    Long getId();
    String getTitle();
    String getAuthor();
    String getPublisher();
    String getBookImg();
    String getIsbn();
}
