package study.back.repository.origin;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import study.back.entity.BookEntity;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookBulkRepository {
    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<BookEntity> bookEntityList) {
        String sql = "insert ignore into books (price, description, author, book_img, isbn, pub_datetime, publisher, title) values (?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql,
                bookEntityList,
                bookEntityList.size(),
                (PreparedStatement ps, BookEntity bookEntity) -> {
                    ps.setInt(1, bookEntity.getPrice());
                    ps.setString(2, bookEntity.getDescription());
                    ps.setString(3, bookEntity.getAuthor());
                    ps.setString(4, bookEntity.getBookImg());
                    ps.setString(5, bookEntity.getIsbn());
                    ps.setString(6, bookEntity.getPubDate());
                    ps.setString(7, bookEntity.getPublisher());
                    ps.setString(8, bookEntity.getTitle());
                }
        );
    }
}
