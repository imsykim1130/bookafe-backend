package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.domain.user.entity.UserEntity;

import java.time.format.DateTimeFormatter;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserItem {
    private Long id;
    private String email;
    private String nickname;
    private String profileImg;
    private String createDate;
    private String role;

    public static UserItem createUserItem(UserEntity user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        UserItem userItem = new UserItem();
        userItem.id = user.getId();
        userItem.email = user.getEmail();
        userItem.nickname = user.getNickname();
        userItem.profileImg = user.getProfileImg();
        userItem.createDate = user.getCreateDate().format(formatter);
        userItem.role = user.getRole().toString();
        return userItem;
    }
}
