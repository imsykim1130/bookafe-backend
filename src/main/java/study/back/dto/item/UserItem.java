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
    private String createDate;
    private String role;

    public static UserItem createUserItem(UserEntity user) {
        UserItem userItem = new UserItem();
        userItem.id = user.getId();
        userItem.email = user.getEmail();
        userItem.nickname = user.getNickname();
        userItem.profileImg = user.getProfileImg();
        userItem.createDate = user.getCreateDate().split(" ")[0];
        userItem.role = user.getRole().toString();
        return userItem;
    }
}
