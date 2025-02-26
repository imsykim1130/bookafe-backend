package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteInfoView {
    private Boolean isFavorite;
    private Integer favoriteCount;
}
