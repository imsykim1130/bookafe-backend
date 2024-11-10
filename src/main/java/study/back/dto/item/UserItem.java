package study.back.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.entity.UserEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserItem {
    private Long id;
    private String email;
    private String nickname;
    private String profileImg;

    public static UserItem createUserItem(UserEntity user) {
        UserItem userItem = new UserItem();
        userItem.id = user.getId();
        userItem.email = user.getEmail();
        userItem.nickname = user.getNickname();
        userItem.profileImg = user.getProfileImg();
        return userItem;
    }
}
