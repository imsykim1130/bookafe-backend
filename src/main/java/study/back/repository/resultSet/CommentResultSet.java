package study.back.repository.resultSet;

public interface CommentResultSet {
    Long getId();
    String getIsbn();
    String getContent();
    String getWriteDate();
    String getProfileImg();
    String getNickname();
    String getReplyCount();
    String getEmoji();
}
