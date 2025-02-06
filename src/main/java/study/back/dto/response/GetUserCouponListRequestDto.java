package study.back.dto.response;

import lombok.Getter;
import study.back.repository.resultSet.UserCouponView;

import java.util.List;

@Getter
public class GetUserCouponListRequestDto extends ResponseDto {
    private List<UserCouponView> userCouponViewList;

    public GetUserCouponListRequestDto(String code, String message, List<UserCouponView> userCouponViewList) {
        super(code, message);
        this.userCouponViewList = userCouponViewList;
    }
}