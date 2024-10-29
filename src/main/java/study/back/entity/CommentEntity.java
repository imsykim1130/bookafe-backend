package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
    private String writeDate;
    private Integer favorite_count;
    private Integer reply_count;
    private String emoji;

    @ManyToOne
    @JoinColumn(name = "isbn")
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parent;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "parent",
            cascade = CascadeType.ALL)
    private List<CommentEntity> children;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favorites")
    private Collection<UserEntity> favoriteUsers = new HashSet<>();

    // 비즈니스 로직
    // 대댓글 추가
    public void addChildrenComment(CommentEntity comment) {
        comment.parent = this;
        children.add(comment);
    }
}
