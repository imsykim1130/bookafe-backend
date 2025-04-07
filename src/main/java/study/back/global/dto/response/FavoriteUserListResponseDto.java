package study.back.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.back.utils.item.FavoriteUser;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FavoriteUserListResponseDto {
    private List<FavoriteUser> favoriteUserList;
    private Boolean isEnd;
    private int totalPage;
}
