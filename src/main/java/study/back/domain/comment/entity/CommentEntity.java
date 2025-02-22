package study.back.domain.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.domain.book.entity.BookEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @Column(length = 1000)
    private String content;
    private LocalDateTime writeDate;
    private String emoji;
    private Boolean isDeleted;
    
    @Column(name = "user_id")
    private Long userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn")
    private BookEntity book;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parent;

    @Builder
    public CommentEntity (String content,
                          LocalDateTime writeDate,
                          String emoji,
                          Boolean isDeleted,
                          CommentEntity parent,
                          BookEntity book,
                          Long userId){

        this.content = content;
        this.writeDate = writeDate;
        this.emoji = emoji;
        this.isDeleted = isDeleted;
        this.parent = parent;
        this.book = book;
        this.userId = userId;
    }

}
