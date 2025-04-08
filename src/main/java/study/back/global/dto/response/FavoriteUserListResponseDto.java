package study.back.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.back.domain.user.query.FavoriteUserQueryDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FavoriteUserListResponseDto {
    private List<FavoriteUserQueryDto> favoriteUserList;
    private Boolean isEnd;
    private int totalPage;
}
